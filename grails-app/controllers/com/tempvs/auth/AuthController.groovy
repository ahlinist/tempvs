package com.tempvs.auth

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.user.EmailVerification
import com.tempvs.user.User
import com.tempvs.user.UserService
import com.tempvs.user.VerifyService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
import org.springframework.security.authentication.encoding.PasswordEncoder

/**
 * Controller that manages {@link com.tempvs.user.User} registration and authorization.
 */
@GrailsCompileStatic
class AuthController {

    private static final String REGISTER_MESSAGE_SENT = 'auth.register.verification.sent.message'
    private static final String NO_SUCH_USER = 'auth.login.noSuchUser.message'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String AUTH_PATTERN = '/auth'

    static allowedMethods = [index: 'GET', login: 'POST', register: 'POST']

    UserService userService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AjaxResponseHelper ajaxResponseHelper
    PasswordEncoder passwordEncoder
    LinkGenerator grailsLinkGenerator

    def index() {
    }

    def login(LoginCommand command) {
        if (command.validate()) {
            User user = userService.getUserByEmail(command.email)

            if (user) {
                if (passwordEncoder.isPasswordValid(user.password, command.password, null)) {
                    springSecurityService.reauthenticate(command.email, command.password)
                    String referer = request.getHeader('referer')

                    if (referer.contains(AUTH_PATTERN)) {
                        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
                    } else {
                        render ajaxResponseHelper.renderRedirect(referer)
                    }
                } else {
                    render ajaxResponseHelper.renderFormMessage(Boolean.FALSE, NO_SUCH_USER)
                }
            } else {
                render ajaxResponseHelper.renderFormMessage(Boolean.FALSE, NO_SUCH_USER)
            }
        } else {
            render ajaxResponseHelper.renderValidationResponse(command)
        }
    }

    def register(RequestRegistrationCommand command) {
        if (command.validate()) {
            Map properties = [action: REGISTRATION_ACTION, email: command.email]
            EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

            if (!emailVerification.hasErrors()) {
                verifyService.sendEmailVerification(emailVerification)
            }

            render ajaxResponseHelper.renderValidationResponse(emailVerification, REGISTER_MESSAGE_SENT)
        } else {
            render ajaxResponseHelper.renderValidationResponse(command)
        }
    }

    def logout() {
        session.invalidate()
        redirect uri: request.getHeader('referer')
    }
}
