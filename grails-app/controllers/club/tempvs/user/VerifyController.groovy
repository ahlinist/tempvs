package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.access.annotation.Secured

/**
 * Controller for managing {@link EmailVerification} instances.
 */
@Secured('permitAll')
@GrailsCompileStatic
class VerifyController {

    private static final String EMAIL = 'email'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'profileEmail.update.failed.message'

    static allowedMethods = [byEmail: 'GET']

    UserService userService
    VerifyService verifyService
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
                        emailVerification.delete(flush: true)
                        break
                    case UserProfile.simpleName.uncapitalize():
                        profileEmail(UserProfile, emailVerification)
                        emailVerification.delete(flush: true)
                        break
                    case ClubProfile.simpleName.uncapitalize():
                        profileEmail(ClubProfile, emailVerification)
                        emailVerification.delete(flush: true)
                        break
                }
            } else {
                error([notFoundMessage: NO_VERIFICATION_CODE])
            }
        } else {
            error([notFoundMessage: NO_VERIFICATION_CODE])
        }
    }

    private registration(EmailVerification emailVerification) {
        render view: 'registration', model: [emailVerification: emailVerification]
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
        Profile profile = profileService.getProfile(clazz, emailVerification.instanceId)

        if (profile) {
            profile = profileService.editProfileField(profile, PROFILE_EMAIL, emailVerification.email)

            if (profile.validate()) {
                profileService.setCurrentProfile(profile)
                return redirect(controller: 'profile', action: profile.shortName, id: profile.identifier)
            }
        }

        error([notFoundMessage: PROFILE_EMAIL_UPDATE_FAILED])
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
