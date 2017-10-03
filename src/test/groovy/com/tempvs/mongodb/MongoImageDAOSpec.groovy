package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.ObjectDAOService
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.mongodb.MongoImageDAO}.
 */
class MongoImageDAOSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String HEX_ID = new ObjectId().toString()
    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    def gridFS = Mock GridFS
    def objectId = new ObjectId()
    def inputStream = Mock InputStream
    def gridFSDBFile = Mock GridFSDBFile
    def gridFSFactory = Mock GridFSFactory
    def mongoImageBean = Mock MongoImageBean
    def gridFSInputFile = Mock GridFSInputFile
    def objectDAOService = Mock ObjectDAOService

    MongoImageDAO mongoImageDAO

    def setup() {
        mongoImageDAO = new MongoImageDAO(
                gridFSFactory: gridFSFactory,
                objectDAOService: objectDAOService,
        )
    }

    def cleanup() {

    }

    void "Test get()"() {
        when:
        def result = mongoImageDAO.get(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * objectDAOService.create(ObjectId, HEX_ID) >> objectId
        1 * gridFS.findOne(objectId) >> gridFSDBFile
        1 * objectDAOService.create(MongoImageBean, gridFSDBFile) >> mongoImageBean
        0 * _

        and:
        result == mongoImageBean
    }

    void "Test create()"() {
        when:
        def result = mongoImageDAO.create(inputStream, COLLECTION)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST) >> gridFSInputFile
        1 * objectDAOService.create(MongoImageBean, gridFSInputFile) >> mongoImageBean
        1 * mongoImageBean.save() >> mongoImageBean
        0 * _

        and:
        result == mongoImageBean
    }

    void "Test delete()"() {
        when:
        def result = mongoImageDAO.delete(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * objectDAOService.create(ObjectId, HEX_ID) >> objectId
        1 * gridFS.findOne(objectId) >> gridFSDBFile
        1 * gridFS.remove(gridFSDBFile)
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test delete() with error"() {
        when:
        def result = mongoImageDAO.delete(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * objectDAOService.create(ObjectId, HEX_ID) >> objectId
        1 * gridFS.findOne(objectId) >> gridFSDBFile
        1 * gridFS.remove(gridFSDBFile) >> {throw new Exception()}
        0 * _

        and:
        result == Boolean.FALSE
    }
}
