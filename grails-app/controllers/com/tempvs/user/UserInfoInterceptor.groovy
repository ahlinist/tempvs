package com.tempvs.user

import com.tempvs.communication.FollowingService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

@GrailsCompileStatic
class UserInfoInterceptor {

    UserService userService
    ProfileService profileService
    UserInfoHelper userInfoHelper
    FollowingService followingService
    SpringSecurityService springSecurityService

    UserInfoInterceptor() {
        matchAll()
                .excludes(controller: ~/(image|verify)/)
                .excludes(controller: 'item', action: ~/(deleteImage|deleteItem|deleteGroup|addImage|editItemField|editItemGroupField|linkSource|unlinkSource)/)
                .excludes(controller: 'passport', action: ~/(editPassportField|addItem|removeItem|deletePassport)/)
                .excludes(controller: 'source', action: ~/(getSourcesByPeriod|editSourceField|deleteImage|deleteSource|addImage|deleteComment)/)
                .excludes(controller: 'user', action: ~/(updateEmail|register)/)
    }

    boolean before() {
        if (springSecurityService.loggedIn) {
            User user = userService.currentUser
            Profile profile = user?.currentProfile
            userInfoHelper.setCurrentUser(request, user)
            userInfoHelper.setCurrentProfile(request, profile)
            userInfoHelper.setNewFollowingsCount(request, followingService.getNewFollowingsCount(profile))
        }

        return Boolean.TRUE
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
