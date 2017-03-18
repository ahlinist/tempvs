package com.tempvs.services

<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.user.User
=======
<<<<<<< HEAD
import com.tempvs.domain.user.User
import com.tempvs.image.Image
import com.tempvs.mongodb.MongoImageDAO
=======
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.user.User
>>>>>>> b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
>>>>>>> Updated the image handling logic and tests.
=======
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.user.User
>>>>>>> origin/master
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
<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
=======
<<<<<<< HEAD
    def imageDAO = Mock(MongoImageDAO)
    def inputStream = Mock(InputStream)
    def user = Mock(User)
    def image1 = Mock(Image)
    def image2 = Mock(Image)
    List<Byte> byteList = "test data".bytes

    def setup() {
        service.springSecurityService = springSecurityService
        service.imageDAO = imageDAO
=======
>>>>>>> Updated the image handling logic and tests.
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
=======
>>>>>>> b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
>>>>>>> Updated the image handling logic and tests.
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
=======
<<<<<<< HEAD
        1 * imageDAO.get(collection, query) >> image1
        1 * imageDAO.create(inputStream, collection, AVATAR_PATH) >> image2
        1 * imageDAO.save(image1, [currentAvatar: null])
        1 * imageDAO.save(image2, query.metadata)
=======
>>>>>>> Updated the image handling logic and tests.
=======
>>>>>>> origin/master
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * fileDAOService.create(inputStream, collection, AVATAR_PATH) >> gridFSInputFile
        1 * fileDAOService.save(gridFSDBFile, [currentAvatar: null])
        1 * fileDAOService.save(gridFSInputFile, query.metadata)
<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
=======
>>>>>>> b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
>>>>>>> Updated the image handling logic and tests.
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
<<<<<<< b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * gridFSDBFile.getInputStream() >> byteArrayInputStream
=======
<<<<<<< HEAD
        1 * imageDAO.get(collection, query) >> image1
        1 * image1.bytes >> byteList
=======
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * gridFSDBFile.getInputStream() >> byteArrayInputStream
>>>>>>> b2387cf9508a3ac3a891b576fb53e45aa2dc5d35
>>>>>>> Updated the image handling logic and tests.
=======
        1 * fileDAOService.get(collection, query) >> gridFSDBFile
        1 * gridFSDBFile.getInputStream() >> byteArrayInputStream
>>>>>>> origin/master
        0 * _

        and:
        result == byteList
    }
}
