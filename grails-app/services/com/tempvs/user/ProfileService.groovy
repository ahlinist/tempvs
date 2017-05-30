package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    SpringSecurityService springSecurityService
    ObjectDAO objectDAO
    ObjectFactory objectFactory

    BaseProfile getProfile(Class clazz, String id) {
        Object result = objectDAO.find(clazz, [profileId: id]) ?: objectDAO.get(clazz, id)

        if (result) {
            result as BaseProfile
        }
    }

    BaseProfile updateProfile(BaseProfile profile, Map params) {
        InvokerHelper.setProperties(profile, params)
        profile.save()
        profile
    }

    ClubProfile createClubProfile(Map properties) {
        User user = springSecurityService.currentUser as User
        ClubProfile clubProfile = objectFactory.create(ClubProfile.class) as ClubProfile
	    clubProfile.firstName = properties.firstName
	    clubProfile.lastName = properties.lastName
        clubProfile.nickName = properties.nickName
	    clubProfile.clubName = properties.clubName
        user.addToClubProfiles(clubProfile)?.save()
        clubProfile
    }

    BaseProfile updateProfileEmail(Class clazz, Long instanceId, String profileEmail) {
        BaseProfile profile = objectDAO.get(clazz, instanceId) as BaseProfile
        profile.profileEmail = profileEmail
        profile.save()
        profile
    }
}
