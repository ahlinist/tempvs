package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService

class ProfileHolder {

    SpringSecurityService springSecurityService

    Class clazz
    Long id

    BaseProfile getProfile() {
        if (!clazz && id == null) {
            springSecurityService.currentUser?.userProfile
        } else {
            clazz.get(id)
        }
    }

    void setProfile(BaseProfile profile) {
        User user = springSecurityService.currentUser

        if (user.userProfile == profile || user.clubProfiles.find {it == profile}) {
            this.clazz = profile.class
            this.id = profile.id
        }
    }
}
