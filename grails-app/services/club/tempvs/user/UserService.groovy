package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode

/**
 * Service that manages operations with {@link User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    ProfileService profileService

    User getUser(Long id) {
        User.get id
    }

    User getCurrentUser() {
        null
    }

    Long getCurrentUserId() {
        null
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    Profile getCurrentProfile() {
        getCurrentUser()?.currentProfile
    }

    List<String> getRoles() {
        []
    }

    User register(User user) {

        return user
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    User editUserField(User user, String fieldName, Object fieldValue) {
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
}
