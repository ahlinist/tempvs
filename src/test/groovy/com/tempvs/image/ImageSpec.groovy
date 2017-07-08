package com.tempvs.image

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Image)
class ImageSpec extends Specification {

    private static final String OBJECT_ID = 'objectId'
    private static final String COLLECTION = 'collection'

    def setup() {
    }

    def cleanup() {
    }

    void "Test valid Image creation"() {
        expect:
        new Image(objectId: OBJECT_ID, collection: COLLECTION).validate()

    }

    void "Test invalid Image reation"() {
        expect:
        !new Image().validate()
    }
}
