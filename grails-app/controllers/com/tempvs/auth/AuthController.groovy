package com.tempvs.user

import com.tempvs.domain.BaseObject
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON

class AuthController {
    private static final String REGISTER_MESSAGE_SENT = 'auth.register.verification.sent.message'
    private static final String NO_SUCH_USER = 'auth.login.noSuchUser.message'
    private static final String REGISTRATION_ACTION = 'registration'

    def userService
    def verifyService
    def springSecurityService
    def ajaxResponseService
    def passwordEncoder

    def index() {

    }

    def login(LoginCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                User user = userService.getUserByEmail(command.email)

                if (user) {
                    if (passwordEncoder.isPasswordValid(user.password, command.password, null)) {
                        springSecurityService.reauthenticate(command.email, command.password)
                        render([redirect: g.createLink(controller: 'profile')] as JSON)
                    } else {
                        render([messages: [g.message(code: NO_SUCH_USER)]] as JSON)
                    }
                } else {
                    render([messages: [g.message(code: NO_SUCH_USER)]] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }

    def register(RequestRegistrationCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                Map props = [action: REGISTRATION_ACTION, email: command.email]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), REGISTER_MESSAGE_SENT)
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }
}
