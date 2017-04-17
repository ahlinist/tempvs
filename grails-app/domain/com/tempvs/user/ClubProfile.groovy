package com.tempvs.user

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class ClubProfile extends BaseProfile {

    String nickName
    String clubName

    static belongsTo = [user: User]

    static constraints = {
        lastName nullable: true
        nickName nullable: true

        profileEmail nullable: true, unique: true, email: true, validator: {profileEmail, ClubProfile clubProfile ->
            User user = User.findByEmail(profileEmail)
            UserProfile userProfile = UserProfile.findByProfileEmail(profileEmail)

            !user || (user.userProfile == userProfile) ||
                    !clubProfile || (userProfile.user == clubProfile.user)
        }

        profileId nullable: true, unique: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
        location nullable: true
        clubName nullable: true
    }
}
