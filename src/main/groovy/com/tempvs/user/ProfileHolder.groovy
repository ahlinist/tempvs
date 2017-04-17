package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService

class ProfileHolder {

    SpringSecurityService springSecurityService

    Class clazz
    Long id

    BaseProfile getProfile() {
        User user = springSecurityService.currentUser

        if (user) {
            if (!clazz || !id) {
                user.userProfile
            } else {
                BaseProfile profile = clazz.get(id)

                if (profile == user.userProfile || profile in user.clubProfiles) {
                    profile
                } else {
                    this.profile = null
                    user.userProfile
                }
            }
        }
    }

    void setProfile(BaseProfile profile = null) {
        this.clazz = profile?.class
        this.id = profile?.id
    }
}
