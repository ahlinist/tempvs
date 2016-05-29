package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class UserProfile extends BasePersistent {
    String firstName
    String lastName
    String profileEmail
    String location
    String customId

    static belongsTo = [user: User]

    static constraints = {
        firstName nullable: true
        lastName nullable: true
        profileEmail nullable: true, unique: true
        location nullable: true
        customId nullable: true, unique: true
    }
}
