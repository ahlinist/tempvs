package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
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
    def inputStream = Mock InputStream
    def gridFSDBFile = Mock GridFSDBFile
    def gridFSFactory = Mock GridFSFactory
    def gridFSInputFile = Mock GridFSInputFile

    MongoImageDAO mongoImageDAO

    def setup() {
        GroovySpy(MongoImageBean, global: true)

        mongoImageDAO = new MongoImageDAO(gridFSFactory: gridFSFactory)
    }

    def cleanup() {

    }

    void "Test get()"() {
        when:
        def result = mongoImageDAO.get(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.findOne(_) >> gridFSDBFile
        0 * _

        and:
        result instanceof MongoImageBean
    }

    void "Test create()"() {
        when:
        def result = mongoImageDAO.create(inputStream, COLLECTION)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST) >> gridFSInputFile
        1 * gridFSInputFile.save()
        0 * _

        and:
        result instanceof MongoImageBean
    }

    void "Test delete()"() {
        when:
        def result = mongoImageDAO.delete(COLLECTION, HEX_ID)

        then:
        1 * gridFSFactory.getGridFS(COLLECTION) >> gridFS
        1 * gridFS.findOne(_) >> gridFSDBFile
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
        1 * gridFS.findOne(_) >> gridFSDBFile
        1 * gridFS.remove(gridFSDBFile) >> {throw new Exception()}
        0 * _

        and:
        result == Boolean.FALSE
    }
}
