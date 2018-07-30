package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.ValidationException
import org.springframework.security.access.annotation.Secured

/**
 * Controller for managing {@link EmailVerification} instances.
 */
@Secured('permitAll')
@GrailsCompileStatic
class VerifyController {

    private static final String EMAIL = 'email'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String EMAIL_FIELD = 'email'
    private static final String EMAIL_USED_CODE = 'user.email.used.error'
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
                    case 'profileEmail':
                        profileEmail(emailVerification)
                        emailVerification.delete(flush: true)
                        break
                    default:
                        error([notFoundMessage: NO_VERIFICATION_CODE])
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
        String email = emailVerification.email
        Long userId = emailVerification.instanceId
        User user = userService.getUser userId

        if (user) {
            if (userService.isEmailUnique(email, userId)) {
                user = userService.editUserField(user, EMAIL, emailVerification.email)
            } else {
                user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [email] as Object[], EMAIL_USED_CODE)
            }

            if (!user.hasErrors()) {
                springSecurityService.reauthenticate(email)
                redirect controller: 'user', action: 'edit'
            }
        } else {
            error([notFoundMessage: EMAIL_UPDATE_FAILED])
        }
    }

    private profileEmail(EmailVerification emailVerification) {
        Profile profile = profileService.getProfile(emailVerification.instanceId)

        if (profile) {
            Profile persistentProfile

            try {
                persistentProfile = profileService.editProfileField(profile, PROFILE_EMAIL, emailVerification.email)
            } catch (ValidationException e) {
                return error([notFoundMessage: PROFILE_EMAIL_UPDATE_FAILED])
            }

            if (!persistentProfile.hasErrors()) {
                User user = userService.currentUser
                profileService.setCurrentProfile(user, persistentProfile)
                return redirect(controller: 'profile', action: 'show', id: persistentProfile.identifier)
            }
        }

        error([notFoundMessage: PROFILE_EMAIL_UPDATE_FAILED])
    }

    private error(Map model) {
        render view: 'error', model: model
    }
}
