package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class UserController {
    def userService
    def springSecurityService
    def passwordEncoder

    def index() {
        redirect action: 'show', id: springSecurityService.currentUser.userProfile.customId ?: springSecurityService.currentUser.id
    }

    def register(UserRegisterCommand urc) {
        if (params.register) {
            if (urc.email) {
                if (userService.checkIfUserExists(urc.email)) {
                    return [user: urc, emailUsed:'user.email.used']
                }
            }

            if (!urc.hasErrors()) {
                User user = userService.createUser(urc.properties)

                if (user) {
                    springSecurityService.reauthenticate(urc.email, urc.password)
                    redirect controller: 'user', action: 'show', id: user.id
                }
            } else {
                return [user: urc, registrationFailed: 'user.registration.failed']
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
            }
        }
    }

    def editUser() {
        [user: springSecurityService.currentUser]
    }

    def updateEmail(String email) {
        User currentUser = springSecurityService.currentUser

        if (currentUser.email == email) {
        } else if (currentUser.userProfile.profileEmail == email || !userService.checkIfUserExists(email)) {
            if (userService.updateEmail(email)) {
                flash.emailSuccess = 'user.edit.email.success.message'
            } else {
                flash.emailError = 'user.edit.email.failed.message'
            }
        } else {
            flash.emailError = 'user.edit.email.used.message'
        }

        redirect action: 'editUser'
    }

    def updatePassword(String currentPassword, String newPassword, String repeatNewPassword) {
        User currentUser = springSecurityService.currentUser

        if (currentPassword && newPassword && repeatNewPassword) {
            if (passwordEncoder.isPasswordValid(currentUser.password, currentPassword, null)) {
                if (newPassword == repeatNewPassword) {
                    if (userService.updatePassword(newPassword)) {
                        flash.passwordSuccess = 'user.edit.password.success.message'
                    } else {
                        flash.passwordError = 'user.edit.password.failed.message'
                    }
                } else {
                    flash.passwordError = 'user.edit.password.repeat.message'
                }
            } else {
                flash.passwordError = 'user.edit.password.dontmatch.message'
            }
        } else {
            flash.passwordError = 'user.edit.password.empty.message'
        }

        redirect action: 'editUser'
    }

    def updateName(String firstName, String lastName) {
        if (firstName && lastName) {
            if (userService.updateName(firstName, lastName)) {
                flash.nameSuccess = 'user.edit.name.success.message'
            } else {
                flash.nameError = 'user.edit.name.failed.message'
            }
        } else {
            flash.nameError = 'user.edit.name.empty.message'
        }

        redirect action: 'editUser'
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
        repeatPassword validator: {repPass, urc ->
            return repPass == urc.password
        }
    }
}