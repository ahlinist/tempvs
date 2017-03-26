package com.tempvs.user

import com.tempvs.user.verification.EmailVerification
import grails.converters.JSON

class UserController {
    def userService
    def springSecurityService
    def ajaxResponseService

    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'user.edit.profileEmail.verification.sent.message'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String REGISTER_ACTION = 'register'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String NO_SUCH_USER = 'user.show.noSuchUser.message'

    static defaultAction = "show"

    def show(String id) {
        User currentUser = springSecurityService.currentUser
        String customId = currentUser?.userProfile?.customId

        if (id) {
            if (customId == id || currentUser?.id as String == id) {
                [user: currentUser, id: customId ?: currentUser.id]
            } else {
                User user = userService.getUser(id)

                if (user) {
                    [user: user, id: customId ?: user.id]
                } else {
                    [id: id, message: NO_SUCH_USER, args: [id]]
                }
            }
        } else {
            if (currentUser) {
                redirect action: 'show', id: customId ?: currentUser.id
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
                Map props = [userId: currentUser.id, email: email, action: UPDATE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), UPDATE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def updatePassword(UserPasswordCommand upc) {
        render ajaxResponseService.composeJsonResponse(upc.validate() ? userService.updatePassword(upc.newPassword) : upc, PASSWORD_UPDATED_MESSAGE)
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
                             email: profileEmail,
                             action: UPDATE_PROFILE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(userService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT)
            }
        }
    }

    def register(RegisterCommand rc) {
        if (params.isAjaxRequest) {
            if (rc.validate()) {
                User user = userService.createUser(rc.properties + [email: session.email])

                if (user?.hasErrors()) {
                    render ajaxResponseService.composeJsonResponse(user)
                } else {
                    springSecurityService.reauthenticate(session.email, rc.password)
                    session.email = null
                    render([redirect: g.createLink(controller: 'user', action: 'show')] as JSON)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(rc)
            }
        } else {
            redirect action: 'show'
        }
    }

    def verify(String id) {
        Map message

        if (id) {
            EmailVerification emailVerification = userService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                Long userId = emailVerification.userId

                switch (emailVerification.action) {
                    case REGISTER_ACTION:
                        session.email = email

                        render view: 'register', model: [email: email]
                        break
                    case UPDATE_EMAIL_ACTION:
                        User user = userService.updateEmail(userId, email)

                        if (user?.hasErrors()) {
                            message = [message: EMAIL_UPDATE_FAILED]
                        } else {
                            redirect controller: 'user', action: 'edit'
                        }

                        break
                    case UPDATE_PROFILE_EMAIL_ACTION:
                        UserProfile userProfile = userService.updateProfileEmail(userId, email)

                        if (userProfile?.hasErrors()) {
                            message = [message: PROFILE_EMAIL_UPDATE_FAILED]
                        } else {
                            redirect controller: 'user', action: 'profile'
                        }

                        break
                    default:
                        break
                }

                emailVerification.delete(flush: true)
            } else {
                message = [message: NO_VERIFICATION_CODE]
            }
        } else {
            message = [message: NO_VERIFICATION_CODE]
        }

        message
    }
}

class UserPasswordCommand {

    static passwordEncoder
    static springSecurityService

    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        currentPassword validator: { currPass, upc ->
            passwordEncoder.isPasswordValid(springSecurityService.currentUser.password, currPass, null)
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

class RegisterCommand {
    String firstName
    String lastName
    String password
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        repeatPassword validator: { repPass, urc ->
            repPass == urc.password
        }
    }
}
