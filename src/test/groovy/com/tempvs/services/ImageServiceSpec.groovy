package com.tempvs.services

import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.user.User
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
    def fileDAOService = Mock(FileDAOService)
    def inputStream = Mock(InputStream)
    def user = Mock(User)
    def gridFSDBFile = Mock(GridFSDBFile)
    def gridFSInputFile = Mock(GridFSInputFile)
    List<Byte> byteList = "test data".bytes
    def byteArrayInputStream = new ByteArrayInputStream(byteList as byte[])

    def setup() {
        service.springSecurityService = springSecurityService
        service.fileDAOService = fileDAOService
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
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * fileDAOService.create(inputStream, collection, AVATAR_PATH) >> gridFSInputFile
        1 * fileDAOService.save(gridFSDBFile, [currentAvatar: null])
        1 * fileDAOService.save(gridFSInputFile, query.metadata)
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
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * gridFSDBFile.getInputStream() >> byteArrayInputStream
        0 * _

        and:
        result == byteList
    }
}
