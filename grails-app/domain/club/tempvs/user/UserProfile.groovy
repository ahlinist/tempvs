package club.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Contains basic {@link User} information.
 */
@GrailsCompileStatic
class UserProfile extends Profile {

    static belongsTo = [user: User]

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false, size: 0..35
        lastName nullable: false, blank: false, size: 0..35
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER, size: 0..35
        location nullable: true, size: 0..35
        avatar nullable: true
        profileEmail nullable: true, unique: true, email: true, size: 0..35
    }
}
