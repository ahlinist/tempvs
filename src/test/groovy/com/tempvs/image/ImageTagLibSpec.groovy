package com.tempvs.image

import com.tempvs.common.CommonTagLib
import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class ImageTagLibSpec extends Specification implements TagLibUnitTest<ImageTagLib> {

    private static final String ID = 'id'
    private static final String OBJECT_ID = 'objectId'
    private static final String IMAGE_URL = '/image/get'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String FIELD_NAME = 'fieldName'
    private static final String INFO_LABEL = 'infoLabel'
    private static final String COLLECTION = 'collection'
    private static final String HORIZONTAL = 'horizontal'
    private static final String IMAGE_LABEL = 'imageLabel'

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
        2 * image.getProperty(COLLECTION) >> COLLECTION
        1 * image.getProperty(OBJECT_ID) >> ID
        1 * image.getProperty(IMAGE_INFO) >> IMAGE_INFO
        0 * _

        and:
        result.contains IMAGE_URL
    }

    void "Test imageUploader()"() {
        given:
        mockTagLib(CommonTagLib)
        Map properties = [fieldName: FIELD_NAME, imageLabel: IMAGE_LABEL, infoLabel: INFO_LABEL]
        String fileInputField = "<input type=\"file\" class=\"col-sm-12 tempvs-form-field\" name=\"${FIELD_NAME}[0].image\""

        when:
        String result = tagLib.imageUploader(properties)

        then:
        0 * _

        and:
        result.contains fileInputField
    }

    void "Test carousel()"() {
        given:
        Map properties = [images:[image, image], orientation: HORIZONTAL]
        String imageTag = "<img style=\"\" class=\"collection ${HORIZONTAL} center-block\" src=\"/image/get/${OBJECT_ID}?collection=${COLLECTION}\" alt=\"${IMAGE_INFO}\"/>"

        when:
        String result = tagLib.carousel(properties)

        then:
        4 * image.getProperty(COLLECTION) >> COLLECTION
        2 * image.getProperty(OBJECT_ID) >> OBJECT_ID
        4 * image.getProperty(IMAGE_INFO) >> IMAGE_INFO
        0 * _

        and:
        result.contains imageTag
    }
}
