package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import grails.compiler.GrailsCompileStatic

/**
 * Instance of this class is HTTP session scoped. Holds info
 * about currently chosen {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileHolder {

    UserService userService
    ObjectDAO objectDAO

    Class clazz
    Long id

    BaseProfile getProfile() {
        User user = userService.currentUser

        if (user) {
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
