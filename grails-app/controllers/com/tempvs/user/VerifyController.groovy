package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

/**
 * Controller for managing {@link com.tempvs.user.EmailVerification} instances.
 */
@GrailsCompileStatic
class VerifyController {

    private static final String EMAIL = 'email'
    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'profileEmail.update.failed.message'

    static allowedMethods = [byEmail: 'GET']

    UserService userService
    VerifyService verifyService
    ProfileHolder profileHolder
    ProfileService profileService
    SpringSecurityService springSecurityService

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
                    case UserProfile.simpleName.uncapitalize():
                        profileEmail(UserProfile, emailVerification)
                        break
                    case ClubProfile.simpleName.uncapitalize():
                        profileEmail(ClubProfile, emailVerification)
                        break
                }

                emailVerification.delete(flush: true)
            } else {
                error([notFoundMessage: NO_VERIFICATION_CODE])
            }
        } else {
            error([notFoundMessage: NO_VERIFICATION_CODE])
        }
    }

    private registration(EmailVerification emailVerification) {
        String email = emailVerification.email
        session.setAttribute(EMAIL, email)
        render view: 'registration', model: [email: email]
    }

    private email(EmailVerification emailVerification) {
        User user = userService.getUser(emailVerification.instanceId)

        if (user) {
            user = userService.editUserField(user, EMAIL, emailVerification.email)

            if (!user.hasErrors()) {
                springSecurityService.reauthenticate(user.email)
                redirect controller: 'user', action: 'edit'
            }
        } else {
            error([notFoundMessage: EMAIL_UPDATE_FAILED])
        }
    }

    private profileEmail(Class clazz, EmailVerification emailVerification) {
        BaseProfile profile = profileService.getProfile(clazz, emailVerification.instanceId)

        if (profile) {
            profile = profileService.editProfileField(profile, 'profileEmail', emailVerification.email)

            if (profile.validate()) {
                profileHolder.profile = profile
                return redirect(controller: 'profile', action: clazz.simpleName.uncapitalize(), id: profile.id)
            }
        }

        error([notFoundMessage: PROFILE_EMAIL_UPDATE_FAILED])
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
