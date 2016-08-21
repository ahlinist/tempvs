package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.util.Holders

class UserController {
    def userService
    def springSecurityService
    def passwordEncoder

    static defaultAction = "show"

    def register(UserRegisterCommand urc) {
        if (params.register) {
            if (!urc.hasErrors()) {
                User user = userService.createUser(urc.properties)

                if (!user.hasErrors()) {
                    springSecurityService.reauthenticate(urc.email, urc.password)
                    redirect controller: 'user'
                }
            } else {
                [user: urc]
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
        User user = userService.updateEmail(email)
        render view: 'edit', model: [user: user, emailSuccess: user.hasErrors() ? '' : 'user.edit.email.success.message']
    }

    def updatePassword(UserPasswordCommand upc) {
        String success = ''

        if (upc.validate()) {
            userService.updatePassword(upc.newPassword)
            success = 'user.edit.password.success.message'
        }

        render view: 'edit', model: [upc: upc, user: springSecurityService.currentUser, passwordSuccess: success]
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