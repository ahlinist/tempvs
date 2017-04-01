package com.tempvs.user

class UserTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String fullName = { attrs ->
        UserProfile userProfile = attrs.user.userProfile
        out << "${userProfile.firstName} ${userProfile.lastName}"
    }
}
