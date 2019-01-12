package club.tempvs.image

import com.netflix.discovery.EurekaClient

class ImageTagLib {

    private static final String IMAGE_SERVICE_NAME = 'image'

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    EurekaClient eurekaClient

    String image = { Map attrs ->
        Image image = attrs.image as Image
        String serviceUrl = eurekaClient.getApplication(IMAGE_SERVICE_NAME)?.instances?.find()?.homePageUrl
        String link = serviceUrl + "/api/image?id=${image?.objectId}"
        Map model = [template: '/image/templates/image', model: [src: link, alt: image?.imageInfo, styles: attrs.styles]]
        out << render(model)
    }
}
