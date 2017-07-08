package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.image.ImageBean
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.mongodb.MongoImageDAO}.
 */
class MongoImageDAOSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String HEX_ID = new ObjectId().toString()
    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    def gridFSFactory = Mock(GridFSFactory)
    def gridFS = Mock(GridFS)
    def gridFSDBFile = Mock(GridFSDBFile)
    def gridFSInputFile = Mock(GridFSInputFile)
    def dBObjectFactory = Mock(DBObjectFactory)
    def imageBeanFactory = Mock(MongoImageBeanFactory)
    def image = Mock(ImageBean)
    def dbObject = Mock(BasicDBObject)
    def inputStream = Mock(InputStream)

    MongoImageDAO mongoImageDAO

    def setup() {
        mongoImageDAO = new MongoImageDAO(
                gridFSFactory: gridFSFactory,
                dBObjectFactory: dBObjectFactory,
                imageBeanFactory: imageBeanFactory,
        )
    }

    def cleanup() {

    }

    void "Test get()"() {
        when:
        def result = mongoImageDAO.get(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.findOne(_ as ObjectId) >> gridFSDBFile
        1 * imageBeanFactory.createInstance(gridFSDBFile) >> image
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
        1 * imageBeanFactory.createInstance(gridFSInputFile) >> image
        1 * image.save() >> image
        0 * _

        and:
        result == image
    }

    void "Test delete()"() {
        when:
        def result = mongoImageDAO.delete(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.findOne(_ as ObjectId) >> gridFSDBFile
        1 * gridFS.remove(gridFSDBFile)
        0 * _

        and:
        result == Boolean.TRUE
    }
}
