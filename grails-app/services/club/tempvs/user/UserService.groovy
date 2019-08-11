package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.userdetails.GrailsUser
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.GrantedAuthority

/**
 * Service that manages operations with {@link User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    ProfileService profileService
    VerifyService verifyService
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

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    Profile getCurrentProfile() {
        getCurrentUser()?.currentProfile
    }

    List<String> getRoles() {
        GrailsUser grailsUser = springSecurityService.principal as GrailsUser
        Collection<GrantedAuthority> grantedAuthorities = grailsUser?.authorities
        grantedAuthorities?.collect { GrantedAuthority grantedAuthority -> grantedAuthority.authority }
    }

    User register(User user) {
        if (user.save()) {
            EmailVerification emailVerification = verifyService.getRegistrationVerificationByUser(user)
            emailVerification.delete()
        }

        return user
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#user.email == authentication.name')
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
