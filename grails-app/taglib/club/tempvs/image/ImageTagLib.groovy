package club.tempvs.image

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator

/**
 * {@link ImageBean} handling taglib.
 */
@GrailsCompileStatic
class ImageTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator

    String image = { Map attrs, Closure body ->
        Image image = attrs.image as Image
        String styles = attrs.styles as String
        String classes = "${image?.collection} center-block"
        String link = grailsLinkGenerator.link(controller: 'image', action: 'get', id: image?.objectId, params: [collection: image?.collection])
        Map model = [template: '/image/templates/image', model: [src: link, classes: classes, alt: image?.imageInfo, styles: styles, body: body()]]
        out << groovyPageRenderer.render(model)
    }

    String modalImage = { Map attrs, Closure body ->
        out << groovyPageRenderer.render(template: '/image/templates/modalImage', model: attrs + [body: body()])
    }

    String imageUploader = { Map attrs ->
        out << groovyPageRenderer.render(template: '/image/templates/imageUploader', model: attrs)
    }

    String modalCarousel = { Map attrs ->
        List<Image> images = attrs.images as List<Image>
        String slideMapping = images.collectEntries { [(images.indexOf(it)): it.id] } as JSON
        Map model = [template: '/image/templates/modalCarousel', model: attrs + [slideMapping: slideMapping]]
        out << groovyPageRenderer.render(model)
    }
}
