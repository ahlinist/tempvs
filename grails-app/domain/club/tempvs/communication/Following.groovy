package club.tempvs.communication

import club.tempvs.domain.BasePersistent
import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic

/**
 * An object that represents cross-{@link club.tempvs.user.Profile} relations.
 */
@GrailsCompileStatic
class Following implements BasePersistent {

    Profile follower
    Profile followed
    Boolean isNew = Boolean.TRUE

    static constraints = {
        follower validator: { Profile follower, Following following ->
            follower != following.followed
        }

        followed validator: { Period period, Following following ->
            following.follower.period == following.followed.period
        }
    }

    static mapping = {
        id composite: ['follower', 'followed']
    }

    int hashCode() {
        (follower.id + followed.id).hashCode()
    }

    boolean equals(Object obj) {
        Following object = (Following) obj

        if (object.follower != this.followed) {
            return false
        }

        return true
    }
}
