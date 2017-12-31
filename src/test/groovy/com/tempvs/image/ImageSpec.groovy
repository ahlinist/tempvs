package com.tempvs.image

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ImageSpec extends Specification implements DomainUnitTest<Image> {

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
