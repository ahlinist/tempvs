package com.tempvs.controllers

import com.tempvs.domain.user.User

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class UserController {
    def userService

    def index() {
        redirect action: 'show', id: session.user.userProfile.customId ?: session.user.id
    }

    def register(UserRegisterCommand urc) {
        if (userService.checkIfUserExists(urc.email)) {
            render view: 'login', model: [user: urc, registerActive: true, emailUsed:'user.email.used']
            return
        }

        if (!urc.hasErrors()) {
            User user = userService.createUser(urc.properties)

            if (user) {
                session.user = user
                redirect action: 'show'
            }
        }

        render view: 'login', model: [user: urc, registerActive: true, registrationFailed: 'user.registration.failed']
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
        [user: session.user]
    }

    def updateUserProfile() {
        if (session.user.email == params.profileEmail ||
                session.user.userProfile.profileEmail == params.profileEmail ||
                !userService.checkIfUserExists(params.profileEmail)) {
            User user = userService.updateUserProfile(session.user.id, params)

            if (user.userProfile) {
                session.user = user
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
        User user
        def multiPartFile = request.getFile('avatar')

        if (!multiPartFile?.empty) {
            user = userService.updateAvatar(session.user.id, multiPartFile)

            if (user) {
                session.user = user
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(new File(userService.getAvatar(session.user.id))), "jpg", baos );
        byte[] imageInByte = baos.toByteArray();
        response.setHeader('Content-length', imageInByte.length.toString())
        response.contentType = 'image/jpg' // or the appropriate image content type
        response.outputStream << imageInByte
        response.outputStream.flush()
    }

    def editUser() {
        [user: session.user]
    }

    def updateEmail(String email) {
        if (session.user.email == email) {
        } else if (session.user.userProfile.profileEmail == email || !userService.checkIfUserExists(email)) {
            User user = userService.updateEmail(session.user.id, email)

            if (user) {
                session.user = user
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
        if (currentPassword && password && repeatPassword) {
            User user = userService.getUser(session.user.id)
            if (user.password == userService.encrypt(currentPassword)) {
                if (password == repeatPassword) {
                    if (userService.updatePassword(user.id, password)) {
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
            User user = userService.updateName(session.user.id, firstName, lastName)

            if (user) {
                session.user = user
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