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
        expect:
        !new ImageUploadCommand().validate()

        and:
        new ImageUploadCommand(imageUploadBean: imageUploadBean).validate()
    }
}
