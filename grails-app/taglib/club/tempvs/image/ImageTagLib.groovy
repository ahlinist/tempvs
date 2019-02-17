package club.tempvs.image

class ImageTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    String image = { Map attrs ->
        Image image = attrs.image as Image
        String link = "/api/image/image/${image?.objectId}"
        Map model = [template: '/image/templates/image', model: [src: link, alt: image?.imageInfo, styles: attrs.styles]]
        out << render(model)
    }
}
