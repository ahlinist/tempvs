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
        Image image = attrs.image
        String classes = "${image?.collection} ${attrs.orientation ?: 'vertical'} center-block"
        String link = grailsLinkGenerator.link(controller: 'image', action: 'get', id: image?.objectId, params: [collection: image?.collection])
        out << render(template: '/image/templates/image', model: [src: link, classes: classes, alt: image?.imageInfo, styles: attrs.styles])
    }

    String modalImage = { Map attrs ->
        out << render(template: '/image/templates/modalImage', model: attrs)
    }

    String imageUploader = { Map attrs ->
        out << render(template: '/image/templates/imageUploader', model: attrs)
    }

    String carousel = { Map attrs ->
        out << render(template: '/image/templates/carousel', model: attrs)
    }
}
