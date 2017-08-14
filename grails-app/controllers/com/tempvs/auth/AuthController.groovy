package com.tempvs.auth

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.User
import com.tempvs.user.UserService
import com.tempvs.user.VerifyService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource
import org.springframework.security.authentication.encoding.PasswordEncoder

/**
 * Controller that manages {@link com.tempvs.user.User} registration and authorization.
 */
@GrailsCompileStatic
class AuthController {

    private static final String REGISTER_MESSAGE_SENT = 'auth.register.verification.sent.message'
    private static final String NO_SUCH_USER = 'auth.login.noSuchUser.message'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String LOGIN_PAGE_URI = '/auth/index'

    UserService userService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService
    PasswordEncoder passwordEncoder
    LinkGenerator grailsLinkGenerator
    MessageSource messageSource

    def index() {
    }

    def login(LoginCommand command) {
        if (command.validate()) {
            User user = userService.getUserByEmail(command.email)

            if (user) {
                if (passwordEncoder.isPasswordValid(user.password, command.password, null)) {
                    springSecurityService.reauthenticate(command.email, command.password)
                    String referer = request.getHeader('referer')

                    if (referer.contains(LOGIN_PAGE_URI)) {
                        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
                    } else {
                        render ajaxResponseService.renderRedirect(referer)
                    }
                } else {
                    render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SUCH_USER)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SUCH_USER)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def register(RequestRegistrationCommand command) {
        if (command.validate()) {
            Map props = [action: REGISTRATION_ACTION, email: command.email]
            render ajaxResponseService.renderValidationResponse(verifyService.createEmailVerification(props), REGISTER_MESSAGE_SENT)
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def logout() {
        request.logout()
        redirect uri: request.getHeader('referer')
    }
}
