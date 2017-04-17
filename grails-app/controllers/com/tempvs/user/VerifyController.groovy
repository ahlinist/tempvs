package com.tempvs.user

class VerifyController {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'profileEmail.update.failed.message'

    VerifyService verifyService
    UserService userService
    ProfileService profileService
    ProfileHolder profileHolder

    static defaultAction = 'byEmail'

    def byEmail(String id) {
        if (id) {
            EmailVerification emailVerification = verifyService.getVerification(id)

            if (emailVerification) {

                switch (emailVerification.action) {
                    case 'registration':
                        registration(emailVerification)
                        break
                    case 'email':
                        email(emailVerification)
                        break
                    case 'userprofile':
                        profileEmail(UserProfile.class, emailVerification)
                        break
                    case 'clubprofile':
                        profileEmail(ClubProfile.class, emailVerification)
                        break
                }

                emailVerification.delete(flush: true)
            } else {
                error([message: NO_VERIFICATION_CODE])
            }
        } else {
            error([message: NO_VERIFICATION_CODE])
        }
    }

    private registration(EmailVerification emailVerification) {
        String email = emailVerification.email
        session.email = email
        render view: 'registration', model: [email: email]
    }

    private email(EmailVerification emailVerification) {
        User user = userService.updateEmail(User.get(emailVerification.instanceId), emailVerification.email)

        if (user?.hasErrors()) {
            error([message: EMAIL_UPDATE_FAILED])
        } else {
            redirect controller: 'user', action: 'edit'
        }
    }

    private profileEmail(Class clazz, EmailVerification emailVerification) {
        BaseProfile profile = profileService.updateProfileEmail(
                clazz.get(emailVerification.instanceId), emailVerification.email)

        if (profile?.hasErrors()) {
            error([message: PROFILE_EMAIL_UPDATE_FAILED])
        } else {
            profileHolder.profile = profile
            redirect controller: 'profile', action: 'edit'
        }
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
