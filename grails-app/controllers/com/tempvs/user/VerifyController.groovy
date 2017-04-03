package com.tempvs.user

class VerifyController {

    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'

    def verifyService

    def registration(String id) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                session.email = email
                emailVerification.delete(flush: true)
                render view: 'registration', model: [email: email]
            } else {
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    def email(String id) {
        Map message

        if (id) {
            EmailVerification emailVerification = userService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                Long userId = emailVerification.userId

                User user = userService.updateEmail(userId, email)

                if (user?.hasErrors()) {
                    message = [message: EMAIL_UPDATE_FAILED]
                } else {
                    redirect controller: 'user', action: 'edit'
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

    def profileEmail(String id) {
        Map message

        if (id) {
            EmailVerification emailVerification = userService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                Long userId = emailVerification.userId

                UserProfile userProfile = userService.updateProfileEmail(userId, email)

                if (userProfile?.hasErrors()) {
                    message = [message: PROFILE_EMAIL_UPDATE_FAILED]
                } else {
                    redirect controller: 'user', action: 'profile'
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

    private def error(Map model) {
        render view: 'error', model: model
    }
}
