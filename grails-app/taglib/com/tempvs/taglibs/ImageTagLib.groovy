package com.tempvs.taglibs

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'
    def springSecurityService

    String userPic = {
        Map model

        if (springSecurityService.currentUser?.userProfile?.avatar?.pathToFile) {
            model = [classes: 'pull-left', src: '/userProfile/getAvatar']
        } else {
            model = [classes: 'pull-left', src: '/assets/defaultAvatar.jpg']
        }

        out << render(template: '/templates/image/userIcon', model:model)
    }
}
