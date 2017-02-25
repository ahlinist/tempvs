package com.tempvs.taglibs

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'
    def springSecurityService

    String userPic = {
        if (springSecurityService.currentUser) {
            out << render(template: '/templates/image/userIcon', model: [classes: 'pull-left', src: '/user/getAvatar'])
        }
    }
}
