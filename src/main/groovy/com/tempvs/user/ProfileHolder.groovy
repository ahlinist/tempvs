package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Instance of this class is HTTP session scoped. Holds info
 * about currently chosen {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileHolder {

    UserService userService
    ProfileService profileService

    Class clazz
    Long id

    BaseProfile getProfile() {
        User user = userService.currentUser

        if (user) {
            UserProfile userProfile = user.userProfile

            if (!clazz || !id) {
                this.profile = userProfile
                userProfile
            } else {
                BaseProfile profile = profileService.getProfile(clazz, id)

                if (profile) {
                    if (profile == userProfile || profile in user.clubProfiles) {
                        profile
                    } else {
                        this.profile = userProfile
                        userProfile
               	    }
		        }
            }
        }
    }

    void setProfile(BaseProfile profile) {
        this.clazz = profile?.class
        this.id = profile?.id
    }
}
