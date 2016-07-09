package com.tempvs.controllers

import com.tempvs.domain.user.User

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

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
                User updatedUser = userService.getUser(id)

                if (updatedUser) {
                    [user: updatedUser, id: updatedUser.userProfile.customId ?: updatedUser.id]
                } else {
                    [id: id, message: "No user with id: ${id}"]
                }
            }
        } else {
            if (currentUser) {
                redirect action: 'show', id: currentUser.userProfile.customId ?: currentUser.id
            } else {
                redirect controller: 'user', action: 'login'
            }
        }
    }

    def editUserProfile() {
        [user: springSecurityService.currentUser]
    }

    def updateUserProfile() {
        User currentUser = springSecurityService.currentUser

        if (currentUser.email == params.profileEmail ||
                currentUser.userProfile.profileEmail == params.profileEmail ||
                !userService.checkIfUserExists(params.profileEmail)) {
            User updatedUser = userService.updateUserProfile(currentUser.id, params)

            if (updatedUser.userProfile) {
                flash.success = 'user.userProfile.updated'
            } else {
                flash.error = 'user.editUserProfile.failed'
            }
        } else {
            flash.error = 'user.editUserProfile.email.used'
        }

        redirect action: 'editUserProfile'
    }

    def updateAvatar() {
        User currentUser = springSecurityService.currentUser
        def multiPartFile = request.getFile('avatar')

        if (!multiPartFile?.empty) {
            User updatedUser = userService.updateAvatar(currentUser.id, multiPartFile)

            if (updatedUser) {
                flash.avatarSuccess = 'user.profile.update.avatar.success.message'
            } else {
                flash.avatarError = 'user.profile.update.avatar.error.message'
            }
        } else {
            flash.avatarError = 'user.profile.update.avatar.empty.message'
        }

        redirect action: 'editUserProfile'
    }

    def getAvatar() {
        User currentUser = springSecurityService.currentUser
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(new File(userService.getAvatar(currentUser.id))), "jpg", baos );
        byte[] imageInByte = baos.toByteArray();
        response.setHeader('Content-length', imageInByte.length.toString())
        response.contentType = 'image/jpg' // or the appropriate image content type
        response.outputStream << imageInByte
        response.outputStream.flush()
    }

    def editUser() {
        [user: springSecurityService.currentUser]
    }

    def updateEmail(String email) {
        User currentUser = springSecurityService.currentUser

        if (currentUser.email == email) {
        } else if (currentUser.userProfile.profileEmail == email || !userService.checkIfUserExists(email)) {
            if (userService.updateEmail(currentUser.id, email)) {
                flash.emailSuccess = 'user.edit.email.success.message'
            } else {
                flash.emailError = 'user.edit.email.failed.message'
            }
        } else {
            flash.emailError = 'user.edit.email.used.message'
        }

        redirect action: 'editUser'
    }

    def updatePassword(String currentPassword, String password, String repeatPassword) {
        User currentUser = springSecurityService.currentUser

        if (currentPassword && password && repeatPassword) {
            if (passwordEncoder.isPasswordValid(currentUser.password, currentPassword, null)) {
                if (password == repeatPassword) {
                    if (userService.updatePassword(currentUser.id, password)) {
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
            if (userService.updateName(springSecurityService.currentUser.id, firstName, lastName)) {
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
        repeatPassword validator: {repPass, urc ->
            return repPass == urc.password
        }
    }
}