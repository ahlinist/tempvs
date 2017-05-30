package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

/**
 * Instance of this class is HTTP session scoped. Holds info
 * about currently chosen {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileHolder {

    SpringSecurityService springSecurityService
    ObjectDAO objectDAO

    Class clazz
    Long id

    BaseProfile getProfile() {
        Object currentUser = springSecurityService.currentUser

        if (currentUser) {
            User user = currentUser as User

            if (!clazz || !id) {
                this.profile = user.userProfile
                user.userProfile
            } else {
                BaseProfile profile = objectDAO.get(clazz, id)

                if (profile) {
                    if (profile == user.userProfile || profile in user.clubProfiles) {
                        profile
                    } else {
                        this.profile = user.userProfile
                        user.userProfile
               	    }
		        }
            }
        }
    }

    void setProfile(BaseProfile profile = null) {
        this.clazz = profile?.class
        this.id = profile?.id
    }
}
