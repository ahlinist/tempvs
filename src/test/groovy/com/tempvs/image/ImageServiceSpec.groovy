package com.tempvs.image

import com.tempvs.mongodb.MongoImageDAO
import grails.test.mixin.TestFor
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

    def imageDAO = Mock(MongoImageDAO)
    def inputStream = Mock(InputStream)
    def image = Mock(Image)

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
        def result = service.createImage(inputStream, collection, metaData)

        then:
        1 * imageDAO.create(inputStream, collection) >> image
        1 * imageDAO.save(image, metaData) >> image

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
}
