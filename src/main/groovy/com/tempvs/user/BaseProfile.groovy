package com.tempvs.user

import com.tempvs.domain.BasePersistent

abstract class BaseProfile extends BasePersistent {
    String firstName
    String lastName
    String profileId

    static constraints = {
        profileId nullable: true, unique: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
    }
}
