package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class ProfileService {

    SpringSecurityService springSecurityService
    ProfileHolder profileHolder

    ClubProfile getClubProfile(String id) {
        try {
            ClubProfile.findByProfileId(id) ?: ClubProfile.get(id as String)
        } catch (NumberFormatException e) {}
    }

    UserProfile getUserProfile(String id) {
        try {
            UserProfile.findByProfileId(id) ?: UserProfile.get(id as Long)
        } catch (NumberFormatException e) {}
    }

    BaseProfile updateProfile(BaseProfile profile, Map params) {
        InvokerHelper.setProperties(profile, params)
        profile.save()
        profile
    }

    ClubProfile createClubProfile(Map properties) {
        User user = springSecurityService.currentUser as User
        ClubProfile clubProfile = new ClubProfile(properties)
        user.addToClubProfiles(clubProfile).save()
        clubProfile
    }

    BaseProfile updateProfileEmail(BaseProfile profile, String profileEmail) {
        profile.profileEmail = profileEmail
        profile.save()

        profile
    }
}
