package club.tempvs.communication

import club.tempvs.domain.BasePersistent
import club.tempvs.user.ClubProfile
import club.tempvs.user.UserProfile
import grails.compiler.GrailsCompileStatic

/**
 * Represents a comment left by {@link User} assigned to {@link Profile} as an owner.
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
        text size: 0..2000
    }
}
