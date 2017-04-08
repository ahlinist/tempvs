package com.tempvs.image

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String avatar = { attrs ->
        Long userId = attrs.profile.user.id
        String profileClass = attrs.profile.class.simpleName
        Long profileId = attrs.profile.id
        String id = "${userId}_${profileClass}_${profileId}"
        String link = g.createLink(controller: 'image', action: 'getAvatar', id: id)
        Map model = [src: link]

        out << render(template: '/templates/image/avatar', model: model)
    }
}
