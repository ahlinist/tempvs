package com.tempvs.image

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImageService)
class ImageServiceSpec extends Specification {

    private static final String ITEM_IMAGE_COLLECTION = 'Item_Image'
    private static final String COLLECTION = 'collection'
    private static final String ID = 'id'
    private static final Long ONE_LONG = 1L
    private static final String AVATAR = 'avatar'

    def imageDAO = Mock(ImageDAO)
    def inputStream = new ByteArrayInputStream()
    def image = Mock(Image)
    def multipartFile = new MockMultipartFile(AVATAR, "1234567" as byte[])

    def setup() {
        service.imageDAO = imageDAO
    }

    def cleanup() {
    }

    void "Test createImage()"() {
        given:
        String collection = ITEM_IMAGE_COLLECTION
        Map metaData = [
                userId: ONE_LONG,
                properties: [itemGroupId: ONE_LONG]
        ]

        when:
        def result = service.createImage(multipartFile, collection, metaData)

        then:
        1 * imageDAO.create(_ as ByteArrayInputStream, collection) >> image
        1 * imageDAO.save(image, metaData) >> image
        0 * _

        and:
        result == image
    }

    void "Test getImage()"() {
        when:
        def result = service.getImage(COLLECTION, ID)

        then:
        1 * imageDAO.get(COLLECTION, ID) >> image
        0 * _

        and:
        result == image
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImages(COLLECTION, [ID, ID])

        then:
        1 * imageDAO.delete(COLLECTION, [ID, ID]) >> Boolean.TRUE
        0 * _

        and:
        result == Boolean.TRUE

        when:
        result = service.deleteImages(COLLECTION, null)

        then:
        0 * _

        and:
        result == Boolean.TRUE
    }
}
