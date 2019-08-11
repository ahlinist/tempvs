package club.tempvs.image

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.converters.JSON
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.gorm.GormStaticApi
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

class ImageServiceSpec extends Specification implements ServiceUnitTest<ImageService>, DomainUnitTest<Image> {

    private static final Long LONG_ID = 1L
    private static final String OBJECT_ID = 'objectId'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'

    def image = Mock Image
    def restCaller = Mock RestCaller
    def restResponse = Mock RestResponse
    def imageUploadBean = Mock ImageUploadBean
    def gormStaticApi = Mock GormStaticApi
    def multipartFile = new MockMultipartFile('1234567', "1234567" as byte[])

    def setup() {
        GroovySpy(Image, global: true)

        service.restCaller = restCaller
    }

    def cleanup() {
    }

    void "Test getImage()"() {
        when:
        def result = service.getImage(LONG_ID)

        then:
        1 * Image.currentGormStaticApi() >> gormStaticApi
        1 * gormStaticApi.get(LONG_ID) >> image
        0 * _

        and:
        result == image
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImage(image)

        then:
        1 * image.objectId >> OBJECT_ID
        1 * restCaller.call(_ as String, HttpMethod.DELETE) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test deleteImages()"() {
        when:
        def result = service.deleteImages([image, image])

        then:
        1 * restCaller.call(_ as String, HttpMethod.POST, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test uploadImage() with image"() {
        when:
        service.uploadImage(imageUploadBean, COLLECTION)

        then:
        2 * imageUploadBean.image >> multipartFile
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * restCaller.call(_ as String, HttpMethod.POST, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> "{}".bytes
        0 * _
    }

    void "Test uploadImages()"() {
        when:
        service.uploadImages([imageUploadBean], COLLECTION)

        then:
        2 * imageUploadBean.image >> multipartFile
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * restCaller.call(_ as String, HttpMethod.POST, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> "{}".bytes
        0 * _
    }
}
