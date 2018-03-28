package club.tempvs.user

import club.tempvs.item.Passport
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * Belongs as many-to-one to {@link User} entity.
 */
@GrailsCompileStatic
class ClubProfile extends Profile {

    String nickName
    String clubName
    Period period
    Collection<Passport> passports

    static belongsTo = [user: User]
    static hasMany = [passports: Passport]

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false, size: 0..35
        lastName nullable: true, size: 0..35
        nickName nullable: true, size: 0..35
        clubName nullable: true, size: 0..35
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER, size: 0..35
        location nullable: true, size: 0..35
        avatar nullable: true
        profileEmail nullable: true, unique: true, email: true, size: 0..35
    }

    @Override
    String toString() {
        "${firstName} ${lastName ?: ''} ${nickName ?: ''}"
    }
}
