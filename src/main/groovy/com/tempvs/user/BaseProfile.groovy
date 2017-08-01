package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import grails.compiler.GrailsCompileStatic

/**
 * Abstract Profile inherited by {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
//@GrailsCompileStatic
abstract class BaseProfile extends BasePersistent {

    Long id
    String firstName
    String lastName
    String profileEmail
    String location
    String profileId
    Image avatar
    User user

    String getIdentifier() {
        profileId ?: id as String
    }

    BaseProfile save(Map params = null) {

    }

    void delete(Map params = null) {

    }

    static constraints = {
        profileId shared: "profileId"
        location nullable: true
        avatar nullable: true
        profileEmail nullable: true, unique: true, email: true, validator: { String profileEmail, BaseProfile baseProfile ->
            if (profileEmail) {
                grails.util.Holders.applicationContext.userService.isEmailUnique(profileEmail)
            }
        }
    }
}
