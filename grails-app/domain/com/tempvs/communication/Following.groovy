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
    Boolean isNew = Boolean.TRUE

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

    int hashCode() {
        profileClassName.hashCode() * followerId * followingId * period.name().hashCode()
    }

    boolean equals(Object obj) {
        Following object = (Following) obj

        if (object.profileClassName != this.profileClassName) {
            return false
        }

        if (object.followerId != this.followerId) {
            return false
        }

        if (object.followingId != this.followingId) {
            return false
        }

        if (object.period != this.period) {
            return false
        }

        return true
    }
}
