package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Contains basic {@link com.tempvs.user.User} information.
 */
@GrailsCompileStatic
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

        profileEmail nullable: true, unique: true, email: true, validator: { String profileEmail, BaseProfile baseProfile ->
            if (profileEmail) {
                baseProfile.userService?.isEmailUnique(profileEmail)
            } else {
                Boolean.TRUE
            }
        }
    }
}
