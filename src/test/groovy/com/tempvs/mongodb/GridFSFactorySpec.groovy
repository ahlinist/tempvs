package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import spock.lang.Specification
/**
 * Test suite for factory that generates {@link com.mongodb.gridfs.GridFS} instances.
 */
class GridFSFactorySpec extends Specification {

    private static final String COLLECTION = 'collection'

    def setup() {

    }

    def cleanup() {

    }

    void "Test getGridFS()"() {
        expect:
        new GridFSFactory().getGridFS(COLLECTION) instanceof GridFS
    }
}
