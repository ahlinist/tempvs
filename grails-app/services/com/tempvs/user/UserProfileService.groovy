package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
@GrailsCompileStatic
class UserProfileService {

    SpringSecurityService springSecurityService

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
        User user = springSecurityService.currentUser as User
        UserProfile userProfile = user?.userProfile
        InvokerHelper.setProperties(userProfile, params)
        userProfile.save(flush: true)
        userProfile
    }
}
