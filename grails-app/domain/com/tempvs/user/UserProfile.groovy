package com.tempvs.user

/**
 * Contains basic {@link com.tempvs.user.User} information.
 */
class UserProfile extends BaseProfile {

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false
        lastName nullable: false, blank: false
        profileId shared: "profileId"
        location nullable: true
        avatar nullable: true

        profileEmail nullable: true, unique: true, email: true, validator: { String profileEmail, UserProfile profile ->
            if (profileEmail) {
                profile.profileService?.isProfileEmailUnique(profile, profileEmail)
            } else {
                Boolean.TRUE
            }
        }
    }
}
