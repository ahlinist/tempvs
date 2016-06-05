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
        redirect action: 'login'
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
        if (session.user) {
            [userProfile: session.user.userProfile]
        } else {
            redirect action: "login"
        }
    }

    def editUser() {
        if (session.user) {
            [user: session.user]
        } else {
            redirect action: "login"
        }
    }

    def saveUserProfile() {
        if (session.user) {
            if (session.user.email == params.profileEmail || session.user.userProfile.profileEmail == params.profileEmail || !userService.checkIfUserExists(params.profileEmail)) {
                UserProfile userProfile = userService.saveUserProfile(session.user?.userProfile?.id, params)

                if (userProfile) {
                    session.user.userProfile = userProfile
                    render view: 'editUserProfile',
                            model: [userProfile: userProfile, userProfileUpdated: 'user.userProfile.updated']
                    return
                } else {
                    render view: 'editUserProfile',
                            model: [userProfile: session.user.userProfile, editUserProfileFailed:'user.editUserProfile.failed']
                }
            } else {
                render view: 'editUserProfile',
                        model: [userProfile: session.user.userProfile, editUserProfileFailed:'user.editUserProfile.email.used']
            }
        } else {
            redirect action: "login"
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
        repeatPassword validator: {repPass, urc ->
            return repPass == urc.password
        }
    }
}