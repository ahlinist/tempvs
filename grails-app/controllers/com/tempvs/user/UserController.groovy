package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BaseObject
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.authentication.encoding.PasswordEncoder

class UserController {
    UserService userService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService

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
            if (!userService.isEmailUnique(email)) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [instanceId: currentUser.id, email: email, action: UPDATE_EMAIL_ACTION]
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
                User user = userService.createUser(command.properties + [email: session.email])

                if (user?.hasErrors()) {
                    render ajaxResponseService.composeJsonResponse(user)
                } else {
                    springSecurityService.reauthenticate(session.email, command.password)
                    session.email = null
                    render([redirect: g.createLink(controller: 'profile')] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        } else {
            redirect controller: 'userProfile'
        }
    }
}

@GrailsCompileStatic
class UserPasswordCommand extends BaseObject {

    static PasswordEncoder passwordEncoder
    static SpringSecurityService springSecurityService

    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        currentPassword validator: { String password, UserPasswordCommand command ->
            User user = springSecurityService.currentUser as User
            passwordEncoder.isPasswordValid(user.password, password, null)
        }
        repeatNewPassword validator: { String password, UserPasswordCommand command ->
            command.newPassword == password
        }
    }
}

@GrailsCompileStatic
class RegisterCommand extends BaseObject {
    String firstName
    String lastName
    String password
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        repeatPassword validator: { String password, RegisterCommand command ->
            password == command.password
        }
    }
}
