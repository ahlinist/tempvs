package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.image.Image
import org.bson.types.ObjectId
import spock.lang.Specification

class MongoImageDAOSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String HEX_ID = new ObjectId().toString()
    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    def gridFSFactory = Mock(GridFSFactory)
    def gridFS = Mock(GridFS)
    def gridFSDBFile = Mock(GridFSDBFile)
    def gridFSInputFile = Mock(GridFSInputFile)
    def dBObjectFactory = Mock(DBObjectFactory)
    def imageFactory = Mock(MongoImageFactory)
    def image = Mock(Image)
    def dbObject = Mock(BasicDBObject)
    def inputStream = Mock(InputStream)

    MongoImageDAO mongoImageDAO

    def setup() {
        mongoImageDAO = new MongoImageDAO(
                gridFSFactory: gridFSFactory,
                dBObjectFactory: dBObjectFactory,
                imageFactory: imageFactory,
        )
    }

    def cleanup() {

    }

    void "Test save()"() {
        given:
        Map metaData = [currentAvatar: Boolean.TRUE]

        when:
        def result = mongoImageDAO.save(image)

        then:
        1 * image.save() >> image
        0 * _

        and:
        result == image

        when:
        result = mongoImageDAO.save(image, metaData)

        then:
        1 * dBObjectFactory.createInstance(metaData) >> dbObject
        1 * image.setMetaData(dbObject)
        1 * image.save() >> image
        0 * _

        and:
        result == image
    }

    void "Test get()"() {
        when:
        def result = mongoImageDAO.get(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.findOne(_ as ObjectId) >> gridFSDBFile
        1 * imageFactory.createInstance(gridFSDBFile) >> image
        0 * _

        and:
        result == image
    }

    void "Test create()"() {
        when:
        def result = mongoImageDAO.create(inputStream, COLLECTION)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST) >> gridFSInputFile
        1 * imageFactory.createInstance(gridFSInputFile) >> image
        0 * _

        and:
        result == image
    }

    void "Test delete()"() {
        when:
        def result = mongoImageDAO.delete(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.remove(_ as ObjectId)
        0 * _

        and:
        result == Boolean.TRUE
    }
}
