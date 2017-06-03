package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    UserService userService
    ObjectDAO<BaseProfile> objectDAO
    ObjectFactory objectFactory

    BaseProfile getProfile(Class clazz, String id) {
        objectDAO.find(clazz, [profileId: id]) ?: objectDAO.get(clazz, id)
    }

    BaseProfile updateProfile(BaseProfile profile, Map params) {
        InvokerHelper.setProperties(profile, params)
        profile.save()
        profile
    }

    ClubProfile createClubProfile(Map properties) {
        User user = userService.currentUser
        ClubProfile clubProfile = objectFactory.create(ClubProfile)
	    clubProfile.firstName = properties.firstName
	    clubProfile.lastName = properties.lastName
        clubProfile.nickName = properties.nickName
	    clubProfile.clubName = properties.clubName
        user.addToClubProfiles(clubProfile)?.save()
        clubProfile
    }

    BaseProfile updateProfileEmail(Class clazz, Long instanceId, String profileEmail) {
        BaseProfile profile = objectDAO.get(clazz, instanceId)
        profile.profileEmail = profileEmail
        profile.save()
        profile
    }
}
