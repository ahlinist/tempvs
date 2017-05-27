package com.tempvs.image

class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String image = { attrs ->
        String link = g.createLink(controller: 'image', action: 'get', id: attrs.objectId, params: [collection: attrs.collection])
        out << render(template: '/templates/image/image', model: [src: link, classList: attrs.collection])
    }
}
