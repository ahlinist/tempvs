package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class UserProfile extends BasePersistent {
    String firstName
    String middleName
    String lastName
    String profileEmail
    String location
    String customId
    String avatar

    static belongsTo = [user: User]

    static constraints = {
        middleName nullable: true
        lastName nullable: true
        profileEmail nullable: true, unique: true, email: true
        location nullable: true
        customId nullable: true, unique: true
        avatar nullable: true
    }
}
