package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import grails.converters.JSON

class AuthController {
    private static final String REGISTER_USER_MESSAGE_SENT = 'user.register.verification.sent.message'
    private static final String NO_SUCH_USER = 'user.login.noSuchUser.message'
    private static final String REGISTER_USER_ACTION = 'registerUser'

    static defaultAction = "login"

    def userService
    def springSecurityService
    def ajaxResponseService
    def passwordEncoder

    def login(LoginCommand lc) {
        if (params.isAjaxRequest) {
            if (lc.validate()) {
                User user = userService.getUserByEmail(lc.email)

                if (user) {
                    if (passwordEncoder.isPasswordValid(user.password, lc.password, null)) {
                        springSecurityService.reauthenticate(lc.email, lc.password)
                        render([redirect: g.createLink(controller: 'user')] as JSON)
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

    def register(RegisterCommand rc) {
        if (params.isAjaxRequest) {
            if (rc.validate()) {
                Map props = rc.properties + [action: REGISTER_USER_ACTION, destination: rc.email]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), REGISTER_USER_MESSAGE_SENT)
            } else {
                render ajaxResponseService.composeJsonResponse(rc)
            }
        }
    }
}

class LoginCommand {
    String email
    String password

    static constraints = {
    }
}

class RegisterCommand {
    String email
    String password
    String firstName
    String lastName
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        email email: true, blank: false, validator: {email, urc ->
            User user = User.findByEmail(email)
            UserProfile userProfile = UserProfile.findByProfileEmail(email)
            EmailVerification emailVerification = EmailVerification.findByEmail(email)
            !user && !userProfile && !emailVerification
        }
        repeatPassword validator: { repPass, urc ->
            repPass == urc.password
        }
    }
}
