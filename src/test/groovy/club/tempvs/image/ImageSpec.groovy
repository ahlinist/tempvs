package club.tempvs.image

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
        given:
        domain.objectId = OBJECT_ID
        domain.collection = COLLECTION

        expect:
        domain.validate()

    }

    void "Test invalid Image reation"() {
        expect:
        !domain.validate()
    }
}
