package com.tempvs.user

import groovy.transform.CompileStatic

import javax.servlet.http.HttpServletRequest

@CompileStatic
class UserInfoHelper {

    private static final String CURRENT_USER = 'currentUser'
    private static final String CURRENT_PROFILE = 'currentProfile'
    private static final String NEW_FOLLOWINGS_COUNT = 'newFollowingsCount'

    ProfileService profileService

    User getCurrentUser(HttpServletRequest request) {
        request.getAttribute(CURRENT_USER) as User
    }

    Profile getCurrentProfile(HttpServletRequest request) {
        request.getAttribute(CURRENT_PROFILE) as Profile
    }

    Integer getNewFollowingsCount(HttpServletRequest request) {
        request.getAttribute(NEW_FOLLOWINGS_COUNT) as Integer
    }

    void setCurrentUser(HttpServletRequest request, User user) {
        request.setAttribute(CURRENT_USER, user)
    }

    void setCurrentProfile(HttpServletRequest request, Profile profile) {
        request.setAttribute(CURRENT_PROFILE, profile)
    }

    void setNewFollowingsCount(HttpServletRequest request, Integer value) {
        request.setAttribute(NEW_FOLLOWINGS_COUNT, value)
    }
}
