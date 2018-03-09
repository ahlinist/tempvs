package com.tempvs.image

import com.tempvs.common.CommonTagLib
import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class ImageTagLibSpec extends Specification implements TagLibUnitTest<ImageTagLib> {

    private static final String ID = 'id'
    private static final String IMAGE_URL = '/image/get'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'

    def image = Mock(Image)

    def setup() {

    }

    def cleanup() {

    }

    void "Test image()"() {
        given:
        Map properties = [image: image]

        when:
        String result = tagLib.image(properties)

        then:
        2 * image.collection >> COLLECTION
        1 * image.objectId >> ID
        1 * image.imageInfo >> IMAGE_INFO
        0 * _

        and:
        result.contains IMAGE_URL
    }
}
