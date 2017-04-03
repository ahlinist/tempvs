package com.tempvs.user

import grails.converters.JSON

class UserController {
    def userService
    def userProfileService
    def verifyService
    def springSecurityService
    def ajaxResponseService

    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'

    def index() {
        redirect controller: 'userProfile'
    }

    def edit() {
        [user: springSecurityService.currentUser]
    }

    def updateEmail(String email) {
        User currentUser = springSecurityService.currentUser

        if (email == currentUser.email) {
            render([messages: [g.message(code: EMAIL_UPDATE_DUPLICATE)]] as JSON)
        } else {
            if (userService.getUserByEmail(email) ||
                    (userProfileService.getProfileByProfileEmail(email) && currentUser.userProfile.profileEmail != email)) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [userId: currentUser.id, email: email, action: UPDATE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), UPDATE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def updatePassword(UserPasswordCommand upc) {
        render ajaxResponseService.composeJsonResponse(upc.validate() ? userService.updatePassword(upc.newPassword) : upc, PASSWORD_UPDATED_MESSAGE)
    }

    def register(RegisterCommand rc) {
        if (params.isAjaxRequest) {
            if (rc.validate()) {
                User user = userService.createUser(rc.properties + [email: session.email])

                if (user?.hasErrors()) {
                    render ajaxResponseService.composeJsonResponse(user)
                } else {
                    springSecurityService.reauthenticate(session.email, rc.password)
                    session.email = null
                    render([redirect: g.createLink(controller: 'userProfile')] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(rc)
            }
        } else {
            redirect controller: 'userProfile'
        }
    }
}

class UserPasswordCommand {

    static passwordEncoder
    static springSecurityService

    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        currentPassword validator: { currPass, upc ->
            passwordEncoder.isPasswordValid(springSecurityService.currentUser.password, currPass, null)
        }
        repeatNewPassword validator: { repPass, upc ->
            upc.newPassword == repPass
        }
    }
}

class RegisterCommand {
    String firstName
    String lastName
    String password
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        repeatPassword validator: { repPass, urc ->
            repPass == urc.password
        }
    }
}
