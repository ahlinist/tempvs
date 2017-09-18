package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
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
    Image avatar
    User user
    protected UserService userService

    String getIdentifier() {
        profileId ?: id as String
    }

    String toString() {
        "${firstName} ${lastName}"
    }

    BaseProfile save(Map params = null) {

    }

    void delete(Map params = null) {

    }

    static belongsTo = [user: User]
    static transients = ['userService']
}
