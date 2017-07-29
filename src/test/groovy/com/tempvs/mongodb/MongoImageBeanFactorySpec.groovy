package com.tempvs.mongodb

import com.mongodb.gridfs.GridFSFile
import spock.lang.Specification

/**
 * A test suite for factory that creates {@link com.tempvs.mongodb.MongoImageBean} instances.
 */
class MongoImageBeanFactorySpec extends Specification {

    def gridFSFile = Mock(GridFSFile)

    def setup() {

    }

    def cleanup() {

    }

    void "Test createInstance()"() {
        expect:
        new MongoImageBeanFactory().createInstance(gridFSFile) instanceof MongoImageBean
    }
}
