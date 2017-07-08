package com.tempvs.image

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {

    private static final String ID = 'id'
    private static final String OBJECT_ID = 'objectId'
    private static final String IMAGE_URL = '/image/get'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'

    def image = Mock(Image)

    def setup() {
    }

    def cleanup() {
    }

    void "UserPic is queried"() {
        given:
        Map properties = [image: image]

        when:
        String result = tagLib.image(properties)

        then:
        2 * image.getProperty(COLLECTION) >> COLLECTION
        1 * image.getProperty(OBJECT_ID) >> ID
        1 * image.getProperty(IMAGE_INFO) >> IMAGE_INFO
        0 * _

        and:
        result.contains IMAGE_URL
    }
}
