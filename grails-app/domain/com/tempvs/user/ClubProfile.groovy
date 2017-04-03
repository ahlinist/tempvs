package com.tempvs.user

class ClubProfile extends BaseProfile {

    String nickName

    static belongsTo = [user: User]

    static constraints = {
        lastName nullable: true
        nickName nullable: true
    }
}
