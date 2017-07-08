package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Contains basic {@link com.tempvs.user.User} information.
 */
@GrailsCompileStatic
class UserProfile extends BaseProfile {

    static belongsTo = [user: User]

    static constraints = {

    }

    static mapping = {
        avatar cascade: 'all-delete-orphan'
    }

    String toString() {
        "${firstName} ${lastName}"
    }
}
