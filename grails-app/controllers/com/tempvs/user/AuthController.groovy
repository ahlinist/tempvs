package com.tempvs.user

import com.tempvs.user.verification.EmailVerification
import grails.converters.JSON

class AuthController {
    private static final String REGISTER_MESSAGE_SENT = 'auth.register.verification.sent.message'
    private static final String NO_SUCH_USER = 'auth.login.noSuchUser.message'
    private static final String REGISTER_ACTION = 'register'

    def userService
    def springSecurityService
    def ajaxResponseService
    def passwordEncoder

    def index() {

    }

    def login(LoginCommand lc) {
        if (params.isAjaxRequest) {
            if (lc.validate()) {
                User user = userService.getUserByEmail(lc.email)

                if (user) {
                    if (passwordEncoder.isPasswordValid(user.password, lc.password, null)) {
                        springSecurityService.reauthenticate(lc.email, lc.password)
                        render([redirect: g.createLink(controller: 'user', action: 'show')] as JSON)
                    } else {
                        render([messages: [g.message(code: NO_SUCH_USER)]] as JSON)
                    }
                } else {
                    render([messages: [g.message(code: NO_SUCH_USER)]] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(lc)
            }
        }
    }

    def register(RequestRegistrationCommand rrc) {
        if (params.isAjaxRequest) {
            if (rrc.validate()) {
                Map props = [action: REGISTER_ACTION, email: rrc.email]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), REGISTER_MESSAGE_SENT)
            } else {
                render ajaxResponseService.composeJsonResponse(rrc)
            }
        }
    }
}

class LoginCommand {
    String email
    String password

    static constraints = {
        email email: true
    }
}

class RequestRegistrationCommand {
    String email

    static constraints = {
        email email: true, blank: false, validator: { email, command ->
            !User.findByEmail(email) && !UserProfile.findByProfileEmail(email) && !EmailVerification.findByEmail(email)
        }
    }
}