package com.tempvs.user

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.validation.Validateable

/**
 * Command object used for binding {@link com.tempvs.user.ClubProfile} properties.
 */
class ClubProfileCommand implements Validateable {

    private static final String PROFILE_ID_MATCHER = /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/

    String firstName
    String lastName
    String location
    String profileId
    String nickName
    String clubName
    Period period
    ImageUploadBean imageUploadBean

    static constraints = {
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        location nullable: true
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER
    }
}
