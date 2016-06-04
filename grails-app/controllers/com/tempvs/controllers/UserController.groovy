package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class UserController {
    def userService

    def index() {
        if (session.user?.attach()) {
            redirect action: 'show', id: session.user.userProfile.customId ?: session.user.id
        } else {
            redirect action: "login"
        }
    }

    def register(UserRegisterCommand urc) {
        if (userService.checkIfUserExists(urc.email)) {
            render view: 'login', model: [user: urc, registerActive: true, emailUsed:'user.email.used']
            return
        }

        if (!urc.hasErrors()) {
            User user = userService.createUser(urc.properties)

            if (user.validate() && user.save()) {
                session.user = user
                redirect action: 'show'
            }
        }

        render view: 'login', model: [user: urc, registerActive: true, registrationFailed:'user.registration.failed']
    }

    def login(String email, String password) {
        if (email && password) {
            User user = userService.getUser(email, password)

            if (user) {
                session.user = user
                redirect action: 'show', id: user.userProfile.customId ?: user.id
            } else {
                render view: 'login', model: [loginFailed:'user.notFound']
            }
        }
    }

    def logout() {
        session.user = null
        redirect uri: "/"
    }

    def show(String id) {
        if (id) {
            User user = userService.getUser(id)

            if (user) {
                [user: user, id: user.userProfile.customId ?: user.id]
            } else {
                [id: id, message:"No user with id: ${id}"]
            }
        } else {
            if (session.user) {
                redirect action: 'show', id: session.user.userProfile.customId ?: session.user.id
            } else {
                redirect action: 'login'
            }
        }
    }

    def editUserProfile() {
        session.user?.attach() ? [userProfile: session.user.userProfile] : redirect(action: "login")
    }

    def saveUserProfile(){
        session.user.userProfile = userService.saveUserProfile(session.user.userProfile.id, params)
        redirect action: 'editUserProfile'
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
    }
}