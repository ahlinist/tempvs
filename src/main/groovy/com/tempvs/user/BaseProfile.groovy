package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image

/**
 * Abstract Profile inherited by {@link com.tempvs.user.UserProfile}
 * or {@link com.tempvs.user.ClubProfile}.
 */
abstract class BaseProfile extends BasePersistent {

    def profileService

    Long id
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

    String toString() {
        "${firstName} ${lastName}"
    }
}
