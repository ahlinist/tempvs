package club.tempvs.communication

import club.tempvs.domain.BasePersistent
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic

/**
 * Represents a comment left by {@link club.tempvs.user.User} assigned to {@link Profile} as an owner.
 */
@GrailsCompileStatic
class Comment implements BasePersistent {

    String text
    Profile profile

    static constraints = {
        text size: 0..2000
    }
}
