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

    private static final String EMAIL = 'email'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String EMAIL_EMPTY = 'user.email.empty'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'

    static allowedMethods = [index: 'GET', edit: 'GET', updateEmail: 'POST', updatePassword: 'POST', register: 'POST']

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

    def updateEmail(String email) {
        if (email) {
            if (email == userService.currentUserEmail) {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_UPDATE_DUPLICATE)
            } else {
                if (!userService.isEmailUnique(email)) {
                    render ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_USED)
                } else {
                    Map properties = [
                            instanceId: userService.currentUserId,
                            email: email,
                            action: UPDATE_EMAIL_ACTION,
                    ]

                    EmailVerification emailVerification = verifyService.createEmailVerification(properties)

                    if (!emailVerification.hasErrors()) {
                        verifyService.sendEmailVerification(emailVerification)
                    }

                    render ajaxResponseService.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT)
                }
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_EMPTY)
        }
    }

    def updatePassword(UserPasswordCommand command) {
        render ajaxResponseService.renderValidationResponse(command.validate() ? userService.updatePassword(command.newPassword) : command, PASSWORD_UPDATED_MESSAGE)
    }

    def register(RegisterCommand command) {
        if (command.validate()) {
            User user = userService.createUser(command.properties + [email: session.getAttribute(EMAIL) as String])

            if (user?.hasErrors()) {
                render ajaxResponseService.renderValidationResponse(user)
            } else {
                springSecurityService.reauthenticate(session.getAttribute(EMAIL) as String, command.password)
                session.setAttribute(EMAIL, null)
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
