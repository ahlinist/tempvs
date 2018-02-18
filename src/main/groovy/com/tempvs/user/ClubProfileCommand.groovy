package com.tempvs.user

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for binding {@link com.tempvs.user.ClubProfile} properties.
 */
@GrailsCompileStatic
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
        firstName size: 0..35
        lastName nullable: true, size: 0..35
        nickName nullable: true, size: 0..35
        clubName nullable: true, size: 0..35
        location nullable: true, size: 0..35
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER, size: 0..35
    }
}
