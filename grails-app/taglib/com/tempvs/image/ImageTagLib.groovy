package com.tempvs.image

import grails.converters.JSON
import grails.web.mapping.LinkGenerator

/**
 * {@link com.tempvs.image.ImageBean} handling taglib.
 */
class ImageTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    LinkGenerator grailsLinkGenerator

    String image = { Map attrs, Closure body ->
        Image image = attrs.image
        String classes = "${image?.collection} ${attrs.orientation ?: 'vertical'} center-block"
        String link = grailsLinkGenerator.link(controller: 'image', action: 'get', id: image?.objectId, params: [collection: image?.collection])
        out << render(template: '/image/templates/image', model: [src: link, classes: classes, alt: image?.imageInfo, styles: attrs.styles, body: body()])
    }

    String modalImage = { Map attrs, Closure body ->
        out << render(template: '/image/templates/modalImage', model: attrs + [body: body()])
    }

    String imageUploader = { Map attrs, Closure body ->
        out << render(template: '/image/templates/imageUploader', model: attrs + [body: body()])
    }

    String carousel = { Map attrs, Closure body ->
        out << render(template: '/image/templates/carousel', model: attrs + [body: body()])
    }

    String modalCarousel = { Map attrs ->
        String slideMapping = attrs.images.collectEntries { [(attrs.images.indexOf(it)): it.id] } as JSON
        out << render(template: '/image/templates/modalCarousel', model: attrs + [slideMapping: slideMapping])
    }
}
