package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    SpringSecurityService springSecurityService
    ObjectDAO objectDAO
    ObjectFactory objectFactory

    User getUser(Object id) {
        objectDAO.get(User, id)
    }

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    String getCurrentUserEmail() {
        if (springSecurityService.loggedIn) {
            ((GrailsUser) springSecurityService.principal).username
        }
    }

    String getCurrentUserPassword() {
        if (springSecurityService.loggedIn) {
            ((GrailsUser) springSecurityService.principal).password
        }
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    @PreAuthorize('!isAuthenticated() or #user.email == authentication.name')
    User saveUser(User user) {
        user.save()
        user
    }

    Boolean isEmailUnique(String email) {
        if (!email || currentUserEmail == email) {
            Boolean.TRUE
        } else {
            UserProfile userProfile = UserProfile.findByProfileEmail(email)
            ClubProfile clubProfile = ClubProfile.findByProfileEmail(email)

            (!userProfile || userProfile.user.email == currentUserEmail) &&
                    (!clubProfile || clubProfile.user.email == currentUserEmail)
        }
    }
}
