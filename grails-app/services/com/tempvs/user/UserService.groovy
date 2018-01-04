package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
@Transactional
class UserService {

    private static String EMAIL_FIELD = 'email'
    private static String EMAIL_USED_CODE = 'user.email.used.error'

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
        if (isEmailUnique(user.email)) {
            user.userProfile = userProfile
            user.save()
        } else {
            user.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [user.email] as Object[], EMAIL_USED_CODE)
        }

        user
    }

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
