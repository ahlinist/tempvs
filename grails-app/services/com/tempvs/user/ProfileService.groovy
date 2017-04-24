package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
@GrailsCompileStatic
class ProfileService {

    SpringSecurityService springSecurityService

    ClubProfile getClubProfile(String id) {
        ClubProfile.findByProfileId(id) ?: ClubProfile.get(id)
    }

    UserProfile getUserProfile(String id) {
        UserProfile.findByProfileId(id) ?: UserProfile.get(id)
    }

    BaseProfile updateProfile(BaseProfile profile, Map params) {
        InvokerHelper.setProperties(profile, params)
        profile.save()
        profile
    }

    ClubProfile createClubProfile(Map properties) {
        User user = springSecurityService.currentUser as User
        ClubProfile clubProfile = new ClubProfile(properties)
        user.addToClubProfiles(clubProfile)?.save()
        clubProfile
    }

    BaseProfile updateProfileEmail(BaseProfile profile, String profileEmail) {
        profile.profileEmail = profileEmail
        profile.save()
        profile
    }
}
