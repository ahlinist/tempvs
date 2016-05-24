package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class UserController {
    def userService

    def index() {
        if (session.user) {
            redirect action: 'show', id: session.user.id
        }
    }

    def register(String email, String password) {
        if (email && password) {
            if (!userService.checkIfUserExists(email)) {
                User user = userService.createUser(email, password)

                if (user) {
                    session.user = user
                    redirect action: "show"
                } else {
                    render view: "index", model: [message: "User has not been saved."]
                }
            } else {
                render view: "index", model: [message: "This E-mail has already been used."]
            }
        } else {
            render view: "index", model: [message: "Please provide either E-mail or Password."]
        }
    }

    def login(String email, String password) {
        if (email && password) {
            User user = userService.getUser(email, password)

            if (user) {
                session.user = user
                redirect action: 'show', id: user.id
            } else {
                render view: "index", model: [message: "User with this login and pass doesn't exist."]
            }
        } else {
            render view: "index", model: [message: "Please provide either E-mail or Password."]
        }
    }

    def logout() {
        session.user = null
        redirect action: 'index'
    }

    def show(String id) {
        if (id) {
            User user = userService.getUser(id)

            if (user) {
                [user: user, id: user.id]
            } else {
                [id: id, message:"No user with id: ${id}"]
            }
        } else {
            if (session.user) {
                [user: session.user, id: session.user.id]
            } else {
                redirect action: 'index'
            }
        }
    }

    def editUserProfile() {
        session.user ? [userProfile: session.user.userProfile] : redirect(action: "index")
    }

    def saveUserProfile(){
        session.user.userProfile = userService.saveUserProfile(session.user.userProfile.id, params)
        redirect action: 'editUserProfile'
    }
}
