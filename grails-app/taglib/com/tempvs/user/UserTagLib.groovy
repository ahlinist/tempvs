package com.tempvs.user

class UserTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'
    def springSecurityService

    String loggedInFullName = {
        UserProfile userProfile = springSecurityService.currentUser.userProfile
        out << "${userProfile.firstName} ${userProfile.lastName}"
    }
}
