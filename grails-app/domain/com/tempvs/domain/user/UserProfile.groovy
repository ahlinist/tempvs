package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class UserProfile extends BasePersistent {
    String firstName
    String lastName
    String profileEmail
    String location

    static belongsTo = [user: User]

    static constraints = {
        firstName nullable: true
        lastName nullable: true
        profileEmail nullable: true
        location nullable: true
        //user nullable: true
    }
}
