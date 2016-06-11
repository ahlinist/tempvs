package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class UserProfile extends BasePersistent {
    String profileEmail
    String location
    String customId
    String avatar

    static belongsTo = [user: User]

    static constraints = {
        profileEmail nullable: true, unique: true
        location nullable: true
        customId nullable: true, unique: true
        avatar nullable: true
    }
}
