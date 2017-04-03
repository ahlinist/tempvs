package com.tempvs.user

import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class UserProfileService {

    def springSecurityService

    UserProfile getProfileByProfileEmail(String profileEmail) {
        UserProfile.findByProfileEmail(profileEmail)
    }

    UserProfile getUserProfile(String id) {
        try {
            UserProfile.findByProfileId(id) ?: UserProfile.get(id as Long)
        } catch (NumberFormatException e) {}
    }

    UserProfile updateProfileEmail(Long userId, String profileEmail) {
        UserProfile userProfile = User.get(userId).userProfile

        if (userProfile) {
            userProfile.profileEmail = profileEmail
            userProfile.save(flush: true)
        }

        userProfile
    }

    UserProfile updateUserProfile(Map params) {
        UserProfile userProfile = springSecurityService.currentUser?.userProfile
        InvokerHelper.setProperties(userProfile, params)
        userProfile.save(flush: true)
        userProfile
    }
}
