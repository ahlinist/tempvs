package com.tempvs.image

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.image.ImageCommand}.
 */
@TestFor(ImageController)
class ImageCommandSpec extends Specification {

    private static final String IMAGE_INFO = 'imageInfo'
    private static final String IMAGE_FILE_NAME = 'imageFileName'

    def multipartFile = new MockMultipartFile(IMAGE_FILE_NAME, "1234567" as byte[])

    def setup() {

    }

    def cleanup() {

    }

    void "Test ImageCommand"() {
        expect:
        !new ImageCommand().validate()

        and:
        !new ImageCommand(imageInfo: IMAGE_INFO).validate()

        and:
        new ImageCommand(image: multipartFile).validate()

        and:
        new ImageCommand(image: multipartFile, imageInfo: IMAGE_INFO).validate()
    }
}
