package com.tempvs.image

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String IMAGE_URL = '/image/get'
    private static final String ID = 'id'

    def setup() {
    }

    def cleanup() {
    }

    void "UserPic is queried"() {
        given:
        Map properties = [objectId: ID, collection: COLLECTION]

        when:
        String result = tagLib.image(properties)

        then:
        result.contains IMAGE_URL
    }
}
