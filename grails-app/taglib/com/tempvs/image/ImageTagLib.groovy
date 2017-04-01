package com.tempvs.image

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String avatar = { attrs ->
        String template = '/templates/image/avatar'
        String link = g.createLink(controller: 'image', action: 'getAvatar', id: attrs.user.id)
        Map model = [src: link]

        out << render(template: template, model: model)
    }
}
