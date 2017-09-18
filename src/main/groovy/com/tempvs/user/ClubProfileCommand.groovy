package com.tempvs.user

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * Command object used for binding {@link com.tempvs.user.ClubProfile} properties.
 */
@GrailsCompileStatic
class ClubProfileCommand extends ProfileCommand {

    String nickName
    String clubName
    Period period
    ImageUploadBean avatarBean

    static constraints = {
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        location nullable: true
        profileId shared: 'profileId'
    }
}
