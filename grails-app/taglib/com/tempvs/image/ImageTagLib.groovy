package com.tempvs.image

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'
    def springSecurityService

    String userPic = {
        if (springSecurityService.currentUser) {
            out << render(template: '/templates/image/userIcon',
                    model: [classes: 'pull-left', src: g.createLink(controller: 'image', action: 'getAvatar')])
        }
    }
}
