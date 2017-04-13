package com.tempvs.user

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class ClubProfile extends BaseProfile {

    String nickName
    String clubEmail
    String clubName

    static belongsTo = [user: User]

    static constraints = {
        lastName nullable: true
        nickName nullable: true

        clubEmail nullable: true, unique: true, email: true, validator: {clubEmail, ClubProfile clubProfile ->
            User user = User.findByEmail(clubEmail)
            UserProfile userProfile = UserProfile.findByProfileEmail(clubEmail)

            !user || (user.userProfile == userProfile) ||
                    !clubProfile || (userProfile.user == clubProfile.user)
        }
    }
}
