package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Controller for managing {@link com.tempvs.user.User}-related instances.
 */
@GrailsCompileStatic
class UserController {

    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String EMAIL = 'email'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'

    UserService userService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService
    MessageSource messageSource
    LinkGenerator grailsLinkGenerator

    def index() {
        redirect controller: 'profile'
    }

    def edit() {
        [user: userService.currentUser]
    }

    def updateEmail(String email) {
        if (email == userService.currentUserEmail) {
            String message = messageSource.getMessage(EMAIL_UPDATE_DUPLICATE, null, EMAIL_UPDATE_DUPLICATE, LocaleContextHolder.locale)
            render([messages: [message]] as JSON)
        } else {
            if (!userService.isEmailUnique(email)) {
                String message = messageSource.getMessage(EMAIL_USED, null, EMAIL_USED, LocaleContextHolder.locale)
                render([messages: [message]] as JSON)
            } else {
                Map props = [instanceId: userService.currentUserId, email: email, action: UPDATE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), UPDATE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def updatePassword(UserPasswordCommand command) {
        render ajaxResponseService.composeJsonResponse(command.validate() ? userService.updatePassword(command.newPassword) : command, PASSWORD_UPDATED_MESSAGE)
    }

    def register(RegisterCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                User user = userService.createUser(command.properties + [email: session.getAttribute(EMAIL) as String])

                if (user?.hasErrors()) {
                    render ajaxResponseService.composeJsonResponse(user)
                } else {
                    springSecurityService.reauthenticate(session.getAttribute(EMAIL) as String, command.password)
                    session.setAttribute(EMAIL, null)
                    render([redirect: grailsLinkGenerator.link(controller: 'profile')] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        } else {
            redirect controller: 'userProfile'
        }
    }
}
