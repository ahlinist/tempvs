package com.tempvs.controllers

import com.tempvs.ajax.AjaxResponse
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import grails.converters.JSON
import grails.util.Holders

import javax.imageio.ImageIO

class UserController {
    def userService
    def springSecurityService
    def imageService
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String REGISTER_USER_MESSAGE_SENT = 'user.register.verification.sent.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'user.edit.profileEmail.verification.sent.message'
    private static final String USER_CREATION_FAILED = 'user.register.userCreation.failed.message'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'userProfile.edit.email.failed.message'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String AVATAR_UPDATED_MESSAGE = 'user.profile.update.avatar.success.message'
    private static final String REGISTER_USER_ACTION = 'registerUser'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'

    static defaultAction = "show"

    def register(UserRegisterCommand urc) {
        if (params.isAjaxRequest) {
            if (urc.validate()) {
                Map props = urc.properties + [action: REGISTER_USER_ACTION, destination: urc.email]
                render new AjaxResponse(userService.createEmailVerification(props), REGISTER_USER_MESSAGE_SENT) as JSON
            } else {
                render new AjaxResponse(urc) as JSON
            }
        }
    }

    def verify(String id) {
        if (id) {
            EmailVerification emailVerification = EmailVerification.findByVerificationCode(id)

            if (emailVerification) {
                switch (emailVerification.action) {
                    case REGISTER_USER_ACTION:
                        User user = userService.createUser(emailVerification.properties)

                        if (user?.hasErrors()) {
                            [message: USER_CREATION_FAILED]
                        } else {
                            springSecurityService.reauthenticate(emailVerification.email, emailVerification.password)
                            redirect controller: 'user'
                        }

                        break
                    case UPDATE_EMAIL_ACTION:
                        User user = userService.updateEmail(emailVerification.destination)

                        if (user?.hasErrors()) {
                            [message: EMAIL_UPDATE_FAILED]
                        } else {
                            redirect controller: 'user', action: 'edit'
                        }

                        break
                    case UPDATE_PROFILE_EMAIL_ACTION:
                        UserProfile userProfile = userService.updateProfileEmail(emailVerification.destination)

                        if (userProfile?.hasErrors()) {
                            [message: PROFILE_EMAIL_UPDATE_FAILED]
                        } else {
                            redirect controller: 'user', action: 'profile'
                        }

                        break
                    default:
                        break
                }

                emailVerification.delete(flush: true)
            } else {
                [message: NO_VERIFICATION_CODE]
            }
        } else {
            [message: NO_VERIFICATION_CODE]
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
        Map props = [email: springSecurityService.currentUser.email,
                     destination: email,
                     action: UPDATE_EMAIL_ACTION]
        render new AjaxResponse(userService.createEmailVerification(props), UPDATE_EMAIL_MESSAGE_SENT) as JSON
    }

    def updatePassword(UserPasswordCommand upc) {
        render new AjaxResponse(upc.validate() ? userService.updatePassword(upc.newPassword) : upc, PASSWORD_UPDATED_MESSAGE) as JSON
    }

    def profile() {
        [user: springSecurityService.currentUser]
    }

    def updateUserProfile(UserProfileCommand upc) {
        render new AjaxResponse(userService.updateUserProfile(upc.properties), USER_PROFILE_UPDATED_MESSAGE) as JSON
    }

    def updateProfileEmail(String profileEmail) {
        Map props = [email: springSecurityService.currentUser.email,
                     destination: profileEmail,
                     action: UPDATE_PROFILE_EMAIL_ACTION]
        render new AjaxResponse(userService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT) as JSON
    }

    def updateAvatar() {
        def multiPartFile = request.getFile('avatar')
        render new AjaxResponse(imageService.updateAvatar(multiPartFile), AVATAR_UPDATED_MESSAGE, multiPartFile) as JSON
    }

    def getAvatar() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(ImageIO.read(new File(imageService.getOwnAvatar().pathToFile)), "jpg", baos)
        byte[] imageInByte = baos.toByteArray()
        response.with{
            setHeader('Content-length', imageInByte.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInByte
            outputStream.flush()
        }
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
    String firstName
    String lastName
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        email email: true, unique: true, blank: false, validator: {email, user ->
            UserProfile userProfile = UserProfile.findByProfileEmail(email)
            EmailVerification emailVerification = EmailVerification.findByEmail(email)
            (!userProfile || (userProfile?.user == user)) && !emailVerification
        }
        repeatPassword validator: { repPass, urc ->
            repPass == urc.password
        }
    }
}

class UserProfileCommand {
    String firstName
    String lastName
    String location
    String customId

    static constraints = {
        importFrom UserProfile
    }
}