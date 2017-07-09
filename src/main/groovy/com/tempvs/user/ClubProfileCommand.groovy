package com.tempvs.user

import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for binding {@link com.tempvs.user.ClubProfile} properties.
 */
@GrailsCompileStatic
class ClubProfileCommand implements Validateable {
    String firstName
    String lastName
    String nickName
    String clubName
    String location
    String profileId
    Period period

    static constraints = {
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        location nullable: true
        profileId shared: "profileId"
    }
}
