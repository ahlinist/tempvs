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
        profileEmail nullable: true, unique: true, email: true, validator: {profileEmail, userProfile ->
            User user = User.findByEmail(profileEmail)
            !user || (user?.userProfile == userProfile)
        }

        location nullable: true
        customId nullable: true, unique: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
    }
}
