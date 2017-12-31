package com.tempvs.user

import com.tempvs.item.Passport
import com.tempvs.periodization.Period

/**
 * Belongs as many-to-one to {@link com.tempvs.user.User} entity.
 */
class ClubProfile extends BaseProfile {

    String nickName
    String clubName
    Period period

    static belongsTo = [user: User]
    static hasMany = [passports: Passport]

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER
        location nullable: true
        avatar nullable: true
        profileEmail nullable: true, unique: true, email: true
    }

    @Override
    String toString() {
        "${firstName} ${lastName ?: ''} ${nickName ?: ''}"
    }
}
