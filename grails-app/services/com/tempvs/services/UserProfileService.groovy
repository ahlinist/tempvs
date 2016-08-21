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

    UserProfile updateAvatar(multiPartFile) {
        UserProfile userProfile = springSecurityService.currentUser?.userProfile

        if (userProfile) {
            String imageName = new Date().time.toString().concat('.jpg')
            String destination = "/home/albvs/storage/grails/images/users/${userProfile.user.id}/avatars/"
            def directory = new File(destination)

            if(!directory.exists()){
                directory.mkdirs()
            }

            multiPartFile.transferTo(new File("${destination}${imageName}"))
            userProfile.avatar = "${destination}${imageName}"
            return userProfile.save()
        }
    }

    String getOwnAvatar() {
        springSecurityService.currentUser?.userProfile?.avatar
    }
}
