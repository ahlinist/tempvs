package com.tempvs.image

import com.tempvs.mongodb.MongoImageDAO
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(ImageService)
class ImageServiceSpec extends Specification {
    private static final String AVATAR_PATH = 'avatar'

    def imageDAO = Mock(MongoImageDAO)
    def inputStream = Mock(InputStream)
    def oldImage = Mock(Image)
    def newImage = Mock(Image)
    List<Byte> byteList = "test data".bytes

    def setup() {
        service.imageDAO = imageDAO
    }

    def cleanup() {
    }

    void "Check updateAvatar()"() {
        given:
        String collection = "${AVATAR_PATH}_1_UserProfile_1"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        when:
        service.updateAvatar(inputStream, collection)

        then:
        1 * imageDAO.get(collection, query) >> oldImage
        1 * imageDAO.create(inputStream, collection, AVATAR_PATH) >> newImage
        1 * imageDAO.save(oldImage, [currentAvatar: null])
        1 * imageDAO.save(newImage, query.metadata)
        0 * _
    }

    void "Check getAvatar()"() {
        given:
        String collection = "${AVATAR_PATH}_1"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        when:
        def result = service.getAvatar('1')

        then:
        1 * imageDAO.get(collection, query) >> oldImage
        1 * oldImage.bytes >> byteList
        0 * _

        and:
        result == byteList
    }
}

