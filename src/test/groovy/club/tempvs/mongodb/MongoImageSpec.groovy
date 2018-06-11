package club.tempvs.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSFile
import spock.lang.Specification

class MongoImageSpec extends Specification {

    private static final String OBJECT_ID = 'objectId'

    def gridFSFile = Mock(GridFSFile)
    def dbObject = Mock(BasicDBObject)

    def setup() {

    }

    def cleanup() {

    }

    void "Test construction"() {
        expect:
        new MongoImageBean(gridFSFile)
    }

    void "Test save()"() {
        given:
        def mongoImage = new MongoImageBean(gridFSFile)

        when:
        mongoImage.save()

        then:
        1 * gridFSFile.save()
        0 * _
    }

    void "Test setMetaData()"() {
        given:
        def mongoImage = new MongoImageBean(gridFSFile)

        when:
        mongoImage.setMetaData(dbObject)

        then:
        1 * gridFSFile.setMetaData(dbObject)
        0 * _
    }

    void "Test getId()"() {
        given:
        def mongoImage = new MongoImageBean(gridFSFile)

        when:
        def result = mongoImage.getId()

        then:
        1 * gridFSFile.getId() >> OBJECT_ID
        0 * _

        and:
        result == OBJECT_ID
    }
}
