package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator

/**
 * Controller for managing {@link com.tempvs.user.User}-related instances.
 */
@GrailsCompileStatic
class UserController {

    private static final String PASSWORD = 'password'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'

    static allowedMethods = [
            index: 'GET',
            edit: 'GET',
            updateEmail: 'POST',
            updatePassword: 'POST',
            register: 'POST',
    ]

    UserService userService
    VerifyService verifyService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService
    SpringSecurityService springSecurityService

    def index() {
        redirect controller: 'profile'
    }

    def edit() {
        [user: userService.currentUser]
    }

    def updateEmail() {
        String email = params.fieldValue

        Map properties = [
                instanceId: userService.currentUserId,
                email: email,
                action: UPDATE_EMAIL_ACTION,
        ]

        EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

        if (!emailVerification.hasErrors()) {
            verifyService.sendEmailVerification(emailVerification)
        }

        render ajaxResponseService.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT)
    }

    def updatePassword(UserPasswordCommand command) {
        if (command.validate()) {
            String password = springSecurityService.encodePassword(command.newPassword)
            User user = userService.editUserField(userService.currentUser, PASSWORD, password)

            if (!user.hasErrors()) {
                render ajaxResponseService.renderFormMessage(Boolean.TRUE, PASSWORD_UPDATED_MESSAGE)
            } else {
                render ajaxResponseService.renderValidationResponse(user)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def register(RegistrationCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseService.renderValidationResponse(command))
        }

        EmailVerification emailVerification = command.emailVerification
        String email = emailVerification.email
        Map properties = command.properties
        properties.email = email
        properties.password = springSecurityService.encodePassword(command.password)
        User user = userService.register(properties as User, properties as UserProfile)

        if (user.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(user))
        }

        springSecurityService.reauthenticate(email)
        emailVerification.delete(flush: true)
        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
    }
}
