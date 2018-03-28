package club.tempvs.user

import club.tempvs.domain.BasePersistent
import club.tempvs.image.Image
import grails.compiler.GrailsCompileStatic
import grails.gorm.dirty.checking.DirtyCheck

/**
 * Abstract Profile inherited by {@link UserProfile} and {@link ClubProfile}.
 */
@DirtyCheck
@GrailsCompileStatic
abstract class Profile implements BasePersistent {

    protected static final String PROFILE_ID_MATCHER = /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/

    Long id
    String firstName
    String lastName
    String profileEmail
    String location
    String profileId
    Boolean active = Boolean.TRUE
    Image avatar
    User user

    String getIdentifier() {
        profileId ?: id as String
    }

    @Override
    String toString() {
        "${firstName} ${lastName}"
    }

    @Override
    boolean equals(Object obj) {
        Profile profile = obj as Profile
        (this.class == profile.class) && (id == profile.id)
    }

    Profile save() {

    }
}
