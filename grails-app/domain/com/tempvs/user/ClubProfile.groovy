package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Belongs as many-to-one to {@link com.tempvs.user.User} entity.
 */
@GrailsCompileStatic
class ClubProfile extends BaseProfile {

    String nickName
    String clubName

    static belongsTo = [user: User]

    static constraints = {
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
    }


    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    String toString() {
        "${firstName} ${lastName ?: ''} ${nickName ?: ''}"
    }
}
