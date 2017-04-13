package com.tempvs.user

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class UserTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String fullName = { Map attrs ->
        Map props = attrs.profile.properties

        out << "${props.firstName} ${props.lastName ?: ''} ${props.nickName ?: ''}"
    }
}
