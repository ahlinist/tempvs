package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import spock.lang.Specification

/**
 * A unit test suite for factory that creates {@link com.mongodb.BasicDBObject} instances.
 */
class DBObjectFactorySpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "Test createInstance()"() {
        given:
        Map query = [:]

        expect:
        new DBObjectFactory().createInstance(query) instanceof BasicDBObject
    }
}
