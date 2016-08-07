package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class UserProfile extends BasePersistent {
    String firstName
    String lastName
    String profileEmail
    String location
    String customId
    String avatar

    static belongsTo = [user: User]

    static constraints = {
        lastName nullable: true
        profileEmail nullable: true, unique: true, email: true
        location nullable: true
        customId nullable: true, unique: true, matches: /^\d*[a-zA-Z.][a-zA-Z\d.]*$/
        avatar nullable: true
    }
}
