package com.tempvs.image

import com.tempvs.domain.ObjectFactory
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImageService)
class ImageServiceSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String ID = 'id'
    private static final Long ONE_LONG = 1L
    private static final String AVATAR = 'avatar'
    private static final List<Byte> BYTE_LIST = "test data".bytes

    def image = Mock(Image)
    def imageDAO = Mock(ImageDAO)
    def imageBean = Mock(ImageBean)
    def objectFactory = Mock(ObjectFactory)
    def multipartFile = new MockMultipartFile(AVATAR, "1234567" as byte[])

    def setup() {
        service.imageDAO = imageDAO
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test getImageBytes()"() {
        when:
        def result = service.getImageBytes(COLLECTION, ID)

        then:
        1 * imageDAO.get(COLLECTION, ID) >> imageBean
        1 * imageBean.bytes >> BYTE_LIST
        0 * _

        and:
        result == BYTE_LIST
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImage(COLLECTION, ID)

        then:
        1 * imageDAO.delete(COLLECTION, ID) >> Boolean.TRUE
        0 * _

        and:
        result == Boolean.TRUE

        when:
        result = service.deleteImage(image)

        then:
        1 * image.collection >> COLLECTION
        1 * image.objectId >> ID
        1 * imageDAO.delete(COLLECTION, ID) >> Boolean.TRUE
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test deleteImages()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.deleteImages(images)

        then:
        2 * image.collection >> COLLECTION
        2 * image.objectId >> ID
        2 * imageDAO.delete(COLLECTION, ID) >> Boolean.TRUE
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test createImage()"() {
        when:
        def result = service.createImage(multipartFile, COLLECTION)

        then:
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        0 * _

        and:
        result == ID
    }

    void "Test replaceImage()"() {
        when:
        def result = service.replaceImage(multipartFile, image)

        then:
        2 * image.collection >> COLLECTION
        1 * image.objectId >> ID
        1 * imageDAO.delete(COLLECTION, ID) >> Boolean.TRUE
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        0 * _

        and:
        result == ID
    }
}
