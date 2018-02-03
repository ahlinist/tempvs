package com.tempvs.communication

import com.tempvs.domain.BasePersistent
import com.tempvs.periodization.Period
import com.tempvs.user.ClubProfile
import grails.compiler.GrailsCompileStatic

/**
 * An object that represents cross-{@link com.tempvs.user.Profile} relations.
 */
@GrailsCompileStatic
class Following implements BasePersistent {

    String profileClassName
    Long followerId
    Long followingId
    Period period

    static constraints = {
        followerId validator: { Long followerId, Following following ->
            followerId != following.followingId
        }

        period nullable: true, validator: { Period period, Following following ->
            if (following.profileClassName == ClubProfile.name) {
                return period != null
            }
        }
    }

    static mapping = {
        id composite: ['profileClassName', 'followerId', 'followingId']
    }
}
