package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * Command object used for binding {@link com.tempvs.user.UserProfile} properties.
 */
@GrailsCompileStatic
class UserProfileCommand extends ProfileCommand {
    static constraints = {
        location nullable: true
        profileId shared: 'profileId'
    }
}
