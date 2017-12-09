package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
class UserService {

    ProfileService profileService
    SpringSecurityService springSecurityService

    User getUser(Long id) {
        User.get id
    }

    User getCurrentUser() {
        springSecurityService.currentUser
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId
    }

    String getCurrentUserEmail() {
        if (springSecurityService.loggedIn) {
            springSecurityService.principal.username
        }
    }

    String getCurrentUserPassword() {
        if (springSecurityService.loggedIn) {
            springSecurityService.principal.password
        }
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User register(User user, UserProfile userProfile) {
        user.userProfile = userProfile
        user.save()
        user
    }

    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        user."${fieldName}" = fieldValue
        user.save()
        user
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
