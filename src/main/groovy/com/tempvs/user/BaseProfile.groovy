package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image

import javax.persistence.MappedSuperclass

/**
 * Abstract Profile inherited by {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
abstract class BaseProfile extends BasePersistent {

    protected static final String PROFILE_ID_MATCHER = /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/

    String firstName
    String lastName
    String profileEmail
    String location
    String profileId
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
        (this.class == obj.class) && (id == obj.id)
    }
}
