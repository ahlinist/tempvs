package com.tempvs.user

class VerifyController {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'

    def verifyService
    def userService
    def userProfileService

    def registration(String id) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                session.email = email
                emailVerification.delete(flush: Boolean.TRUE)
                render view: 'registration', model: [email: email]
            } else {
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    def email(String id) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

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
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    def profileEmail(String id) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

            if (emailVerification) {
                String email = emailVerification.email
                Long userId = emailVerification.userId

                UserProfile userProfile = userProfileService.updateProfileEmail(userId, email)

                if (userProfile?.hasErrors()) {
                    message = [message: PROFILE_EMAIL_UPDATE_FAILED]
                } else {
                    redirect controller: 'userProfile', action: 'edit'
                }

                emailVerification.delete(flush: true)
            } else {
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    private def error(Map model) {
        render view: 'error', model: model
    }
}
