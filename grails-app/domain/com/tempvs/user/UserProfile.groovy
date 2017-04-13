package com.tempvs.user

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserProfile extends BaseProfile {
    String profileEmail
    String location

    static belongsTo = [user: User]

    static constraints = {
        profileEmail nullable: true, unique: true, email: true, validator: {profileEmail, UserProfile userProfile ->
            User user = User.findByEmail(profileEmail)
            ClubProfile clubProfile = ClubProfile.findByClubEmail(profileEmail)

            !user || (user.userProfile == userProfile) ||
                    !clubProfile || (userProfile.user == clubProfile.user)
        }

        location nullable: true
    }
}
