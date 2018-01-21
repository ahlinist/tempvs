package com.tempvs.communication

import com.tempvs.domain.BasePersistent
import com.tempvs.user.ClubProfile
import com.tempvs.user.UserProfile
import grails.compiler.GrailsCompileStatic

/**
 * Represents a comment left by {@link com.tempvs.user.User} assigned to {@link com.tempvs.user.Profile} as an owner.
 */
@GrailsCompileStatic
class Comment implements BasePersistent {

    String text
    UserProfile userProfile
    ClubProfile clubProfile

    static constraints = {
        userProfile nullable: true, validator: { UserProfile userProfile, Comment comment ->
            ClubProfile clubProfile = comment.clubProfile

            if (userProfile) {
                return !clubProfile
            } else if (clubProfile) {
                return !userProfile
            } else {
                return Boolean.FALSE
            }
        }

        clubProfile nullable: true
    }
}
