package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
@GrailsCompileStatic
class UserService {

    ObjectDAOService objectDAOService
    SpringSecurityService springSecurityService

    User getUser(Object id) {
        objectDAOService.get(User, id)
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

    User register(Map properties) {
        properties.userProfile = properties as UserProfile
        properties.password = springSecurityService.encodePassword(properties.password as String)
        User user = properties as User
        objectDAOService.save(user)
    }

    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        InvokerHelper.setProperties(user, ["${fieldName}": fieldValue])
        objectDAOService.save(user)
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
