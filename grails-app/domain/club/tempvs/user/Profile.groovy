package club.tempvs.user

import club.tempvs.domain.BasePersistent
import club.tempvs.image.Image
import club.tempvs.item.Passport
import club.tempvs.periodization.Period
import club.tempvs.profile.ProfileDto
import grails.compiler.GrailsCompileStatic

/**
 * A Profile that contains personal data.
 */
@GrailsCompileStatic
class Profile implements BasePersistent {

    private static final String PROFILE_ID_MATCHER = /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/

    Long id
    String firstName
    String lastName
    String nickName
    String profileEmail
    String location
    String profileId
    Boolean active = Boolean.TRUE
    Image avatar
    String clubName
    Period period
    List<Passport> passports
    ProfileType type

    static belongsTo = [user: User]
    static hasMany = [passports: Passport]

    static mapping = {
        avatar cascade: 'all-delete-orphan'
        passports cascade: 'all-delete-orphan'
    }

    static constraints = {
        firstName nullable: false, blank: false, size: 0..35
        lastName nullable: true, blank: false, size: 0..35
        nickName nullable: true, size: 0..35
        clubName nullable: true, size: 0..35
        profileId nullable: true, unique: true, matches: PROFILE_ID_MATCHER, size: 0..35
        location nullable: true, size: 0..35
        avatar nullable: true
        profileEmail nullable: true, email: true, size: 0..35
        period nullable: true, validator: { Period period, Profile profile ->
            if (profile.type == ProfileType.USER) {
                return Boolean.TRUE
            } else if (!period) {
                return Boolean.FALSE
            }
        }
    }

    String getIdentifier() {
        profileId ?: id as String
    }

    Boolean isOfUserType() {
        type == ProfileType.USER
    }

    Boolean isOfClubType() {
        type == ProfileType.CLUB
    }

    @Override
    String toString() {
        String result = firstName

        if (lastName) {
            result += " ${lastName}"
        }

        if (type == ProfileType.CLUB && nickName) {
            result += " ${nickName}"
        }

        return result.trim()
    }

    @Override
    boolean equals(Object obj) {
        Profile profile = obj as Profile
        return this.id == profile.id
    }

    ProfileDto toProfileDto() {
        new ProfileDto(this)
    }
}
