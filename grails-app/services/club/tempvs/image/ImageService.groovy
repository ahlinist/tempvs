package club.tempvs.image

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import com.netflix.discovery.EurekaClient
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.json.JsonSlurper
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link Image}-related operations.
 */
@Slf4j
@GrailsCompileStatic
class ImageService {

    private static final String IMAGE_SERVICE_NAME = 'image'
    private static final String IMAGE_SECURITY_TOKEN = System.getenv 'IMAGE_SECURITY_TOKEN'

    RestCaller restCaller
    EurekaClient eurekaClient

    Image getImage(Long id) {
        Image.get id
    }

    Boolean deleteImage(Image image) {
        if (!image) {
            return Boolean.TRUE
        }

        String serviceUrl = eurekaClient.getApplication(IMAGE_SERVICE_NAME)?.instances?.find()?.homePageUrl
        String url = "${serviceUrl}/api/image/${image.objectId}"
        RestResponse response = restCaller.call(url, HttpMethod.DELETE, IMAGE_SECURITY_TOKEN?.encodeAsMD5() as String)
        HttpStatus statusCode = response?.statusCode
        Boolean success = (statusCode == HttpStatus.OK)

        if (!response) {
            log.warn "Image deletion failed."
        } else if (!success) {
            String message = "Image deletion failed. Image service returned status code: '${statusCode.value()}'.\n"

            if (response?.responseBody) {
                message += "Response body: ${response.responseBody}"
            }

            log.warn message
        }

        return success
    }

    Boolean deleteImages(List<Image> images) {
        String serviceUrl = eurekaClient.getApplication(IMAGE_SERVICE_NAME)?.instances?.find()?.homePageUrl
        String url = serviceUrl + '/api/image/delete'
        String token = IMAGE_SECURITY_TOKEN?.encodeAsMD5() as String
        JSON payload = [images: images] as JSON

        RestResponse response = restCaller.call(url, HttpMethod.POST, token, payload)
        HttpStatus statusCode = response?.statusCode
        Boolean success = (statusCode == HttpStatus.OK)

        if (!success) {
            String message = "Image deletion failed. Image service returned status code: '${statusCode.value()}'.\n"

            if (response.responseBody) {
                message += "Response body: ${response.responseBody}"
            }

            log.warn message
        }

        return success
    }

    Image uploadImage(ImageUploadBean imageUploadBean, String collection) {
        uploadImages([imageUploadBean], collection)?.find()
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Image> uploadImages(List<ImageUploadBean> imageUploadBeans, String collection) {
        if (imageUploadBeans.every { it.image.empty }) {
            return []
        }

        List<Image> images = []
        String serviceUrl = eurekaClient.getApplication(IMAGE_SERVICE_NAME)?.instances?.find()?.homePageUrl
        String url = serviceUrl + '/api/image'
        String token = IMAGE_SECURITY_TOKEN?.encodeAsMD5() as String
        List<Map<String, String>> entries = []

        for (ImageUploadBean imageUploadBean in imageUploadBeans) {
            MultipartFile multipartFile = imageUploadBean.image

            if (!multipartFile.empty) {
                String content = multipartFile.bytes.encodeBase64().toString()
                entries << [fileName: multipartFile.originalFilename, imageInfo: imageUploadBean.imageInfo, content: content]
            }
        }

        JSON payload = [images: entries] as JSON

        RestResponse response = restCaller.call(url, HttpMethod.POST, token, payload)
        HttpStatus statusCode = response?.statusCode

        if (statusCode == HttpStatus.OK) {
            JsonSlurper slurper = new JsonSlurper()
            def json = slurper.parseText(response.responseBody)

            for (imageNode in json.images) {
                images << new Image(objectId: imageNode.objectId, collection: "none", imageInfo: imageNode.imageInfo)
            }
        } else if (statusCode) {
            log.error "Image upload failed. Image service returns status code '${statusCode}'."
        }

        return images
    }
}
