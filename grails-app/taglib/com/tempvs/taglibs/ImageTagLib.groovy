package com.tempvs.taglibs

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = 'tempvs'
    def springSecurityService

    String userPic = {
        def result

        if (springSecurityService.currentUser.userProfile.avatar.pathToFile) {
            result = '<img class="pull-left" src="/userProfile/getAvatar" />'
        } else {
            result = '<img src="/assets/defaultAvatar.jpg" class="pull-left"/>'
        }

        out << result
    }
}
