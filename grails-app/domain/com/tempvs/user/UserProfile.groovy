package com.tempvs.user

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserProfile extends BaseProfile {

    static belongsTo = [user: User]

    static constraints = {
        profileEmail nullable: true, unique: true, email: true, validator: {profileEmail, UserProfile userProfile ->
            User user = User.findByEmail(profileEmail)
            ClubProfile clubProfile = ClubProfile.findByProfileEmail(profileEmail)

            !user || (user.userProfile == userProfile) ||
                    !clubProfile || (userProfile.user == clubProfile.user)
        }
        profileId nullable: true, unique: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
        location nullable: true
    }
}
