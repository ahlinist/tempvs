package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import grails.converters.JSON
import grails.util.Holders
import org.springframework.util.StreamUtils

class UserController {
    def userService
    def springSecurityService
    def imageService
    def ajaxResponseService
    def assetResourceLocator

    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'user.edit.profileEmail.verification.sent.message'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String AVATAR_UPDATED_MESSAGE = 'user.profile.update.avatar.success.message'
    private static final String AVATAR_UPDATED_FAILED_MESSAGE = 'user.profile.update.avatar.failed.message'
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    private static final String REGISTER_USER_ACTION = 'registerUser'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String NO_SUCH_USER = 'user.show.noSuchUser.message'
    private static final String DEFAULT_AVATAR = 'defaultAvatar.jpg'

    static defaultAction = "show"

    def verify(String id) {
        Map result = [:]

        if (id) {
            EmailVerification emailVerification = userService.getVerification(id)

            if (emailVerification) {
                switch (emailVerification.action) {
                    case REGISTER_USER_ACTION:
                        session.emailVerification = emailVerification
                        result = [createUser: Boolean.TRUE ]

                        break
                    case UPDATE_EMAIL_ACTION:
                        User user = userService.updateEmail(emailVerification.userId, emailVerification.destination)

                        if (user?.hasErrors()) {
                            result = [message: EMAIL_UPDATE_FAILED]
                        } else {
                            emailVerification.delete(flush: true)
                            redirect controller: 'user', action: 'edit'
                        }

                        break
                    case UPDATE_PROFILE_EMAIL_ACTION:
                        UserProfile userProfile = userService.updateProfileEmail(emailVerification.userId, emailVerification.destination)

                        if (userProfile?.hasErrors()) {
                            result = [message: PROFILE_EMAIL_UPDATE_FAILED]
                        } else {
                            emailVerification.delete(flush: true)
                            redirect controller: 'user', action: 'profile'
                        }

                        break
                    default:
                        break
                }
            } else {
                result = [message: NO_VERIFICATION_CODE]
            }
        } else {
            result = [message: NO_VERIFICATION_CODE]
        }

        result
    }

    def createUser(String firstName, String lastName) {
        EmailVerification emailVerification = session.emailVerification

        User user = userService.createUser(emailVerification.properties +
                [firstName: firstName, lastName: lastName])

        if (user?.hasErrors()) {
            render ajaxResponseService.composeJsonResponse(user)
        } else {
            emailVerification.delete(flush: true)
            springSecurityService.reauthenticate(emailVerification.email, emailVerification.password)
            render([redirect: g.createLink(controller: 'user', action: 'show')] as JSON)
        }
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
                    [id: id, message: NO_SUCH_USER, args: [id]]
                }
            }
        } else {
            if (currentUser) {
                redirect action: 'show', id: currentUser.userProfile.customId ?: currentUser.id
            } else {
                redirect controller: 'auth', action: 'login'
            }
        }
    }

    def edit() {
        [user: springSecurityService.currentUser]
    }

    def updateEmail(String email) {
        User currentUser = springSecurityService.currentUser

        if (email == currentUser.email) {
            render([messages: [g.message(code: EMAIL_UPDATE_DUPLICATE)]] as JSON)
        } else {
            if (userService.getUserByEmail(email) ||
                    (userService.getUserByProfileEmail(email) && currentUser.userProfile.profileEmail != email)) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [userId: currentUser.id,
                             destination: email,
                             action: UPDATE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), UPDATE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def updatePassword(UserPasswordCommand upc) {
        render ajaxResponseService.composeJsonResponse(upc.validate() ? userService.updatePassword(upc.newPassword) : upc, PASSWORD_UPDATED_MESSAGE)
    }

    def profile() {
        [user: springSecurityService.currentUser]
    }

    def updateUserProfile(UserProfileCommand upc) {
        render ajaxResponseService.composeJsonResponse(userService.updateUserProfile(upc.properties), USER_PROFILE_UPDATED_MESSAGE)
    }

    def updateProfileEmail(String profileEmail) {
        User currentUser = springSecurityService.currentUser

        if (profileEmail == currentUser.userProfile.profileEmail) {
            render([messages: [g.message(code: EMAIL_UPDATE_DUPLICATE)]] as JSON)
        } else {
            if (userService.getUserByEmail(profileEmail) && currentUser.email != profileEmail ||
                    (userService.getUserByProfileEmail(profileEmail))) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [userId: currentUser.id,
                             destination: profileEmail,
                             action: UPDATE_PROFILE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def updateAvatar() {
        Boolean success
        String message
        def multiPartFile = request.getFile('avatar')

        if (!multiPartFile?.empty) {
            InputStream inputStream = multiPartFile.inputStream

            try {
                imageService.updateAvatar(inputStream)
                success = Boolean.TRUE
                message = AVATAR_UPDATED_MESSAGE
            } catch (Exception e) {
                success = Boolean.FALSE
                message = AVATAR_UPDATED_FAILED_MESSAGE
            } finally {
                inputStream?.close()
            }
        } else {
            success = Boolean.FALSE
            message = IMAGE_EMPTY
        }

        render ajaxResponseService.renderMessage(success, message)
    }

    def getAvatar() {
        byte[] imageInBytes = imageService.getOwnAvatar() ?:
                assetResourceLocator?.findAssetForURI(DEFAULT_AVATAR)?.getInputStream()?.bytes ?:
                        StreamUtils.emptyInput()

        response.with{
            setHeader('Content-length', imageInBytes.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInBytes
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

class UserProfileCommand {
    String firstName
    String lastName
    String location
    String customId

    static constraints = {
        importFrom UserProfile
    }
}
