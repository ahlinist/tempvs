package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
@GrailsCompileStatic
class UserService {

    ProfileService profileService
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

    User register(User user, UserProfile userProfile) {
        user.userProfile = userProfile
        objectDAOService.save(user)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        user."${fieldName}" = fieldValue
        objectDAOService.save(user)
    }

    Boolean isEmailUnique(String email) {
        UserProfile userProfile = profileService.getProfileByProfileEmail(UserProfile, email)
        ClubProfile clubProfile = profileService.getProfileByProfileEmail(ClubProfile, email)

        if (userProfile && userProfile.user.email != email) {
            return Boolean.FALSE
        }

        if (clubProfile && clubProfile.user.email != email) {
            return Boolean.FALSE
        }

        return Boolean.TRUE
    }
}
