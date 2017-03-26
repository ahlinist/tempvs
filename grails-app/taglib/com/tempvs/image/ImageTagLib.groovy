package com.tempvs.image

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String loggedInUserPic = {
        String template = '/templates/image/userPic'
        String link = g.createLink(controller: 'image', action: 'getAvatar')
        Map model = [classes: 'pull-left', src: link]

        out << render(template: template, model: model)
    }
}
