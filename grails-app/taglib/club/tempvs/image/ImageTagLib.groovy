package club.tempvs.image

import grails.compiler.GrailsCompileStatic
import grails.gsp.PageRenderer

@GrailsCompileStatic
class ImageTagLib {

    private static final String IMAGE_SERVICE_URL = System.getenv "IMAGE_SERVICE_URL"

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    PageRenderer groovyPageRenderer

    String image = { Map attrs ->
        Image image = attrs.image as Image
        String link = IMAGE_SERVICE_URL + "/api/get?id=${image?.objectId}&collection=${image?.collection}"
        Map model = [template: '/image/templates/image', model: [src: link, alt: image?.imageInfo, styles: attrs.styles]]
        out << groovyPageRenderer.render(model)
    }
}
