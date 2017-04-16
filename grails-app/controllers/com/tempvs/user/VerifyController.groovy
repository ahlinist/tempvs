package com.tempvs.user

class VerifyController {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'profileEmail.update.failed.message'

    def verifyService
    def userService
    ProfileService profileService
    ProfileHolder profileHolder

    def registration(String id) {
        Closure action = { emailVerification ->
            String email = emailVerification.email
            session.email = email
            render view: 'registration', model: [email: email]
        }

        verify(id, action)
    }

    def email(String id) {
        Closure action = { emailVerification ->
            User user = userService.updateEmail(User.get(emailVerification.instanceId), emailVerification.email)

            if (user?.hasErrors()) {
                message = [message: EMAIL_UPDATE_FAILED]
            } else {
                redirect controller: 'user', action: 'edit'
            }
        }

        verify(id, action)
    }

    def userprofile(String id) {
        profileEmail(UserProfile.class, id)
    }

    def clubprofile(String id) {
        profileEmail(ClubProfile.class, id)
    }

    private profileEmail(Class clazz, String id) {
        Closure action = { emailVerification ->
            BaseProfile profile = profileService.updateProfileEmail(
                    clazz.get(emailVerification.instanceId), emailVerification.email)

            if (profile?.hasErrors()) {
                error([message: PROFILE_EMAIL_UPDATE_FAILED])
            } else {
                profileHolder.profile = profile
                redirect controller: 'profile', action: 'edit'
            }
        }

        verify(id, action)
    }

    private verify(String id, Closure closure) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

            if (emailVerification) {

                closure.call(emailVerification)

                emailVerification.delete(flush: true)
            } else {
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
