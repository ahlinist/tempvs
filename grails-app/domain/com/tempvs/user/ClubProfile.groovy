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
        clubName nullable: true

        profileEmail nullable: true, unique: true, email: true, validator: { String profileEmail, ClubProfile clubProfile ->
            User user = User.findByEmail(profileEmail)
            UserProfile userProfile = UserProfile.findByProfileEmail(profileEmail)

            !user || (user.userProfile == userProfile) ||
                    !userProfile || (userProfile.user == clubProfile.user)
        }
    }
}
