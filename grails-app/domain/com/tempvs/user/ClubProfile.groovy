package com.tempvs.user

import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * Belongs as many-to-one to {@link com.tempvs.user.User} entity.
 */
@GrailsCompileStatic
class ClubProfile extends BaseProfile {

    String nickName
    String clubName
    Period period

    static constraints = {
        firstName nullable: false, blank: false
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        profileId shared: "profileId"
        location nullable: true
        avatar nullable: true

        profileEmail nullable: true, unique: true, email: true, validator: { String profileEmail, ClubProfile profile ->
            if (profileEmail) {
                profile.profileService?.isProfileEmailUnique(profile, profileEmail)
            } else {
                Boolean.TRUE
            }
        }
    }

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    String toString() {
        "${firstName} ${lastName ?: ''} ${nickName ?: ''}"
    }
}
