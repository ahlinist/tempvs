package com.tempvs.image

import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.User
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(ImageService)
class ImageServiceSpec extends Specification {
    private static final String ID = 'id'
    private static final String AVATAR_PATH = 'avatar'

    def springSecurityService = Mock(SpringSecurityService)
    def imageDAO = Mock(MongoImageDAO)
    def inputStream = Mock(InputStream)
    def user = Mock(User)
    def image1 = Mock(Image)
    def image2 = Mock(Image)
    List<Byte> byteList = "test data".bytes

    def setup() {
        service.springSecurityService = springSecurityService
        service.imageDAO = imageDAO
    }

    def cleanup() {
    }

    void "Check updateAvatar()"() {
        given:
        String collection = "${AVATAR_PATH}_1"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        when:
        service.updateAvatar(inputStream)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(ID) >> 1
        1 * imageDAO.get(collection, query) >> image1
        1 * imageDAO.create(inputStream, collection, AVATAR_PATH) >> image2
        1 * imageDAO.save(image1, [currentAvatar: null])
        1 * imageDAO.save(image2, query.metadata)
        0 * _
    }

    void "Check getOwnAvatar()"() {
        given:
        String collection = "${AVATAR_PATH}_1"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        when:
        def result = service.getOwnAvatar()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(ID) >> 1
        1 * imageDAO.get(collection, query) >> image1
        1 * image1.bytes >> byteList
        0 * _

        and:
        result == byteList
    }
}

