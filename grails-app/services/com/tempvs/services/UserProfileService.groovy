package com.tempvs.services

import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class UserProfileService {
    def springSecurityService

    UserProfile updateUserProfile(Map params) {
        UserProfile userProfile = springSecurityService.currentUser?.userProfile
        InvokerHelper.setProperties(userProfile, params)
        userProfile.save()
        return userProfile
    }
}
