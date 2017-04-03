package com.tempvs.user

class UserTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String fullName = { attrs ->
        Map props = attrs.profile.properties

        out << "${props.firstName} ${props.lastName ?: ''} ${props.nickName ?: ''}"
    }
}
