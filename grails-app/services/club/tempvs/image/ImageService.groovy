package club.tempvs.image

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.json.JsonSlurper
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link Image}-related operations.
 */
@Slf4j
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

        RestResponse response = restCaller.doDelete(url, payload, headers)
        Integer statusCode = response?.statusCode
        Boolean success = (statusCode == HttpStatus.OK.value())

        if (!success) {
            log.warn "Image deletion failed. Image service returned status code: '${statusCode}'.\n" +
                    " Response body: ${response.responseBody}"
        }

        return success
    }

    Image uploadImage(ImageUploadBean imageUploadBean, String collection) {
        uploadImages([imageUploadBean], collection)?.find()
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Image> uploadImages(List<ImageUploadBean> imageUploadBeans, String collection) {
        List<Image> images = []
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

        RestResponse response = restCaller.doPost(url, payload, headers)
        Integer statusCode = response?.statusCode

        if (statusCode == HttpStatus.OK.value()) {
            JsonSlurper slurper = new JsonSlurper()
            def json = slurper.parseText(response.responseBody)

            for (imageNode in json.images) {
                images << new Image(objectId: imageNode.objectId, collection: imageNode.collection, imageInfo: imageNode.imageInfo)
            }
        } else if (statusCode) {
            log.error "Image upload failed. Image service returns status code '${statusCode}'."
        }

        return images
    }
}
