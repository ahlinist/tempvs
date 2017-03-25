package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSFile
import spock.lang.Specification

class MongoImageSpec extends Specification {

    def gridFSFile = Mock(GridFSFile)
    def dbObject = Mock(BasicDBObject)

    def setup() {

    }

    def cleanup() {

    }

    void "Test construction"() {
        expect:
        new MongoImage(gridFSFile)
    }

    void "Test save()"() {
        given:
        def mongoImage = new MongoImage(gridFSFile)

        when:
        mongoImage.save()

        then:
        1 * gridFSFile.save()
        0 * _
    }

    void "Test setMetaData()"() {
        given:
        def mongoImage = new MongoImage(gridFSFile)

        when:
        mongoImage.setMetaData(dbObject)

        then:
        1 * gridFSFile.setMetaData(dbObject)
        0 * _
    }

    void "Test getBytes()"() {
        given:
        List<Byte> byteList = "test data".bytes
        def inputStream = GroovyMock(InputStream)
        def gridFSDBFile = Mock(GridFSDBFile)
        def mongoImage = new MongoImage(gridFSDBFile)

        when:
        List<Byte> result = mongoImage.getBytes()

        then:
        1 * gridFSDBFile.getInputStream() >> inputStream
        1 * inputStream.getBytes() >> byteList
        0 * _

        and:
        result == byteList
    }
}
