package com.tempvs.controllers

import com.tempvs.ajax.AjaxResponse
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.converters.JSON
import grails.util.Holders

class UserController {
    def userService
    def springSecurityService
    private static final String EMAIL_UPDATED_MESSAGE = 'user.edit.email.success.message'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'

    static defaultAction = "show"

    def register(UserRegisterCommand urc) {
        if (params.isAjaxRequest) {
            if (urc.validate()) {
                User user = userService.createUser(urc.properties)

                if (!user?.hasErrors()) {
                    springSecurityService.reauthenticate(urc.email, urc.password)
                    render([redirect:'/user/show'] as JSON)
                } else {
                    render new AjaxResponse(user) as JSON
                }
            } else {
                render new AjaxResponse(urc) as JSON
            }
        }
    }

    def login() {
    }

    def show(String id) {
        User currentUser = springSecurityService.currentUser

        if (id) {
            if (currentUser?.userProfile?.customId == id || currentUser?.id as String == id) {
                [user: currentUser, id: currentUser.userProfile.customId ?: currentUser.id]
            } else {
                User user = userService.getUser(id)

                if (user) {
                    [user: user, id: user.userProfile.customId ?: user.id]
                } else {
                    [id: id, message: "No user with id: ${id}"]
                }
            }
        } else {
            if (currentUser) {
                redirect action: 'show', id: currentUser.userProfile.customId ?: currentUser.id
            } else {
                redirect action: 'login'
            }
        }
    }

    def edit() {
        [user: springSecurityService.currentUser]
    }

    def updateEmail(String email) {
        render new AjaxResponse(userService.updateEmail(email), EMAIL_UPDATED_MESSAGE) as JSON
    }

    def updatePassword(UserPasswordCommand upc) {
        render new AjaxResponse(upc.validate() ? userService.updatePassword(upc.newPassword) : upc, PASSWORD_UPDATED_MESSAGE) as JSON
    }
}
class UserPasswordCommand {
    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        currentPassword validator: { currPass, upc ->
            Holders.applicationContext.passwordEncoder.isPasswordValid(
                    Holders.applicationContext.springSecurityService.currentUser.password, currPass, null)
        }
        repeatNewPassword validator: { repPass, upc ->
            upc.newPassword == repPass
        }
    }
}

class UserRegisterCommand {
    String email
    String password
    String repeatPassword
    String firstName
    String lastName

    static constraints = {
        importFrom User
        importFrom UserProfile
        repeatPassword validator: { repPass, urc ->
            repPass == urc.password
        }
    }
}