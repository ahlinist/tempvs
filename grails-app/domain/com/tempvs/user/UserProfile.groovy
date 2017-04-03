package com.tempvs.user

class UserProfile extends BaseProfile {
    String profileEmail
    String location

    static belongsTo = [user: User]

    static constraints = {
        profileEmail nullable: true, unique: true, email: true, validator: {profileEmail, userProfile ->
            User user = User.findByEmail(profileEmail)
            !user || (user?.userProfile == userProfile)
        }

        location nullable: true
    }
}
