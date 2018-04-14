package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.userdetails.GrailsUser
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
    SpringSecurityService springSecurityService

    User getUser(Long id) {
        User.get id
    }

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    String getCurrentUserEmail() {
        if (springSecurityService.loggedIn) {
            GrailsUser grailsUser = springSecurityService.principal as GrailsUser
            grailsUser.username as String
        }
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User register(User user, UserProfile userProfile) {
        if (isEmailUnique(user.email)) {
            user.userProfile = userProfile
            user.save()
        } else {
            user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [user.email] as Object[], EMAIL_USED_CODE)
        }

        user
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        if ((fieldName == EMAIL_FIELD) && !isEmailUnique(fieldValue)) {
            user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [fieldValue] as Object[], EMAIL_USED_CODE)
            return user
        }

        user."${fieldName}" = fieldValue
        user.save()
        user
    }

    Boolean isEmailUnique(String email) {
        Profile userProfile = profileService.getProfileByProfileEmail(UserProfile, email)
        Profile clubProfile = profileService.getProfileByProfileEmail(ClubProfile, email)

        if (userProfile && userProfile.user.email != email) {
            return Boolean.FALSE
        }

        if (clubProfile && clubProfile.user.email != email) {
            return Boolean.FALSE
        }

        return Boolean.TRUE
    }
}
