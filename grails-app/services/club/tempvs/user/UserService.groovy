package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    private static String EMAIL_FIELD = 'email'
    private static String EMAIL_USED_CODE = 'user.email.used.error'

    ProfileService profileService
    VerifyService verifyService
    SpringSecurityService springSecurityService

    User getUser(Long id) {
        User.get id
    }

    boolean isLoggedIn() {
        springSecurityService.loggedIn
    }

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User register(User user) {
        if (isEmailUnique(user.email)) {
            user.save()
        } else {
            user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [user.email] as Object[], EMAIL_USED_CODE)
        }

        EmailVerification emailVerification = verifyService.getVerificationByUser(user)
        emailVerification.delete()

        return user
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        if ((fieldName == EMAIL_FIELD) && !isEmailUnique(fieldValue, user.id)) {
            user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [fieldValue] as Object[], EMAIL_USED_CODE)
            return user
        }

        user."${fieldName}" = fieldValue
        user.save()
        user
    }

    boolean isEmailUnique(String email, Long id = null) {
        List<Profile> profiles = profileService.getProfilesByProfileEmail(email)

        if (id) {
            User persistentUser = getUser(id)

            return !profiles?.any {
                User profileUser = it.user
                (profileUser.id != id) && (profileUser != persistentUser)
            }
        } else {
            return !profiles
        }
    }

    boolean ifAnyRoleGranted(String roles) {
        SpringSecurityUtils.ifAnyGranted(roles)
    }
}
