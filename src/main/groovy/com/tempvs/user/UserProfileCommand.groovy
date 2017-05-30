package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for binding {@link com.tempvs.user.UserProfile} properties.
 */
@GrailsCompileStatic
class UserProfileCommand implements Validateable {
    String firstName
    String lastName
    String location
    String profileId

    static constraints = {
        location nullable: true
        profileId shared: "profileId"
    }
}
