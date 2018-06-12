package club.tempvs.image

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.json.JsonSlurper
import groovy.transform.TypeCheckingMode
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link Image}-related operations.
 */
@GrailsCompileStatic
class ImageService {

    private static final String IMAGE_SERVICE_URL = System.getenv 'IMAGE_SERVICE_URL'
    private static final String IMAGE_SECURITY_TOKEN = System.getenv 'IMAGE_SECURITY_TOKEN'

    RestCaller restCaller

    Image getImage(Long id) {
        Image.get id
    }

    Boolean deleteImage(Image image) {
        deleteImages([image])
    }

    Boolean deleteImages(List<Image> images) {
        String url = IMAGE_SERVICE_URL + '/api/delete'
        Map<String, String> headers = [token: IMAGE_SECURITY_TOKEN.encodeAsMD5() as String]
        JSON payload = [images: images] as JSON

        restCaller.doDelete(url, payload, headers)?.statusCode == HttpStatus.OK.value()
    }

    Image uploadImage(ImageUploadBean imageUploadBean, String collection) {
        uploadImages([imageUploadBean], collection)?.find()
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Image> uploadImages(List<ImageUploadBean> imageUploadBeans, String collection) {
        String url = IMAGE_SERVICE_URL + '/api/store'
        Map<String, String> headers = [token: IMAGE_SECURITY_TOKEN.encodeAsMD5() as String]
        List<Map<String, String>> entries = []

        for (ImageUploadBean imageUploadBean in imageUploadBeans) {
            MultipartFile multipartFile = imageUploadBean.image

            if (!multipartFile.empty) {
                String content = multipartFile.bytes.encodeBase64().toString()
                entries << [collection: collection, imageInfo: imageUploadBean.imageInfo, content: content]
            }
        }

        JSON payload = [images: entries] as JSON

        RestResponse restResponse = restCaller.doPost(url, payload, headers)

        List<Image> images = []

        if (restResponse.statusCode == HttpStatus.OK.value()) {
            JsonSlurper slurper = new JsonSlurper()
            def json = slurper.parseText(restResponse.responseBody)

            for (imageNode in json.images) {
                images << new Image(objectId: imageNode.objectId, collection: imageNode.collection, imageInfo: imageNode.imageInfo)
            }
        }

        return images
    }
}
