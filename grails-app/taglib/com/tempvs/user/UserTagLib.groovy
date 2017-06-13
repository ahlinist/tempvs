package com.tempvs.user

import grails.compiler.GrailsCompileStatic

/**
 * {@link com.tempvs.user.User} taglib.
 */
@GrailsCompileStatic
class UserTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String fullName = { Map attrs ->
        Map props = attrs.profile.properties

        out << "${props.firstName} ${props.lastName ?: ''} ${props.nickName ?: ''}"
    }
}
