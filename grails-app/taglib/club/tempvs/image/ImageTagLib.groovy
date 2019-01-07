package club.tempvs.image

class ImageTagLib {

    private static final String IMAGE_SERVICE_URL = System.getenv "IMAGE_SERVICE_URL"

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String image = { Map attrs ->
        Image image = attrs.image as Image
        String link = IMAGE_SERVICE_URL + "/api/image?id=${image?.objectId}"
        Map model = [template: '/image/templates/image', model: [src: link, alt: image?.imageInfo, styles: attrs.styles]]
        out << render(model)
    }
}
