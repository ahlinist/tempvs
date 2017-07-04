package com.tempvs.image

import grails.web.mapping.LinkGenerator

/**
 * {@link com.tempvs.image.ImageBean} handling taglib.
 */
class ImageTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    LinkGenerator grailsLinkGenerator

    String image = { Map attrs ->
        String link = grailsLinkGenerator.link(controller: 'image', action: 'get', id: attrs.objectId, params: [collection: attrs.collection])
        out << render(template: '/templates/image/image', model: [src: link, classList: attrs.collection])
    }
}
