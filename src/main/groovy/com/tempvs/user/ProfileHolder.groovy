package com.tempvs.user

import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Instance of this class is HTTP session scoped. Holds info
 * about currently chosen {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
class ProfileHolder {

    UserService userService
    ProfileService profileService

    Long id
    Class clazz

    @PostAuthorize('#returnObject == null or #returnObject.user.email == authentication.name')
    BaseProfile getProfile() {
        if (clazz && id) {
            profileService.getProfile(clazz, id)
        } else {
            UserProfile userProfile = userService.currentUser?.userProfile
            this.profile = userProfile
            userProfile
        }
    }

    @PreAuthorize('#profile == null or #profile.user.email == authentication.name')
    void setProfile(BaseProfile profile) {
        this.clazz = profile?.class
        this.id = profile?.id
    }
}
