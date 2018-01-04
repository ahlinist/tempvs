package com.tempvs.user

/**
 * Contains basic {@link com.tempvs.user.User} information.
 */
class UserProfile extends Profile {

    static belongsTo = [user: User]

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false
        lastName nullable: false, blank: false
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER
        location nullable: true
        avatar nullable: true
        profileEmail nullable: true, unique: true, email: true
    }
}
