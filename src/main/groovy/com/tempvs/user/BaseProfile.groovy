package com.tempvs.user

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * Abstract Profile inherited by {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
abstract class BaseProfile extends BasePersistent {
    Long id
    String firstName
    String lastName
    String profileEmail
    String location
    String profileId
    String avatar
    User user

    String getIdentifier() {
        profileId ?: id as String
    }

    Object save(Map params = null) {
    }

    void delete(Map params = null) {
    }

    static constraints = {
        profileId shared: "profileId"
        location nullable: true
        avatar nullable: true
    }
}
