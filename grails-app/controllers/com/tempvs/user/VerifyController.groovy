package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Controller for managing {@link com.tempvs.user.EmailVerification} instances.
 */
@GrailsCompileStatic
class VerifyController {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'profileEmail.update.failed.message'
    private static final String EMAIL = 'email'

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
        session.setAttribute(EMAIL, email)
        render view: 'registration', model: [email: email]
    }

    private email(EmailVerification emailVerification) {
        User user = userService.updateEmail(emailVerification.instanceId, emailVerification.email)

        if (user?.hasErrors()) {
            error([message: EMAIL_UPDATE_FAILED])
        } else {
            redirect controller: 'user', action: 'edit'
        }
    }

    private profileEmail(Class clazz, EmailVerification emailVerification) {
        BaseProfile profile = profileService.editProfileEmail(clazz, emailVerification.instanceId, emailVerification.email)

        if (profile?.hasErrors()) {
            error([message: PROFILE_EMAIL_UPDATE_FAILED])
        } else {
            profileHolder.profile = profile
            redirect controller: 'profile'
        }
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
