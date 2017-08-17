package com.tempvs.image

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * Unit-test suite for {@link ImageUploadCommand}.
 */
@TestFor(ImageController)
class ImageUploadCommandSpec extends Specification {

    def imageUploadBean = Mock(ImageUploadBean)

    def setup() {

    }

    def cleanup() {

    }

    void "Test ImageCommand"() {
        given:
        Map properties = [className: Object.class.name, id: 1L]

        expect:
        !new ImageUploadCommand(properties).validate()

        and:
        new ImageUploadCommand(properties + [imageUploadBean: imageUploadBean]).validate()
    }
}
