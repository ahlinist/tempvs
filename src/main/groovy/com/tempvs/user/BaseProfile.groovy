package com.tempvs.user

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * Abstract Profile inherited by {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
abstract class BaseProfile extends BasePersistent {

    String firstName
    String lastName
    String profileEmail
    String location
    String profileId
    String avatar

    String getIdentifier() {
        profileId ?: id as String
    }

    static constraints = {
        profileId shared: "profileId"
        location nullable: true
        avatar nullable: true
    }
}
