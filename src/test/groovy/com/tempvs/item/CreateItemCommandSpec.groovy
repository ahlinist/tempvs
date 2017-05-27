package com.tempvs.item

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * Unit-test suite for CreateItemCommand.
 */
@TestFor(ItemController)
class CreateItemCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String ITEM_IMAGE_FILE_NAME = 'itemImage'
    private static final String SOURCE_IMAGE_FILE_NAME = 'sourceImage'

    def setup() {

    }

    def cleanup() {

    }

    void "Test CreateItemCommand"() {
        given:
        def itemImage = new MockMultipartFile(ITEM_IMAGE_FILE_NAME, "1234567" as byte[])
        def sourceImage = new MockMultipartFile(SOURCE_IMAGE_FILE_NAME, "1234567" as byte[])

        expect:
        !new CreateItemCommand().validate()

        and:
        new CreateItemCommand(name: NAME).validate()

        and:
        new CreateItemCommand(name: NAME, itemImage: itemImage, sourceImage: sourceImage).validate()
    }
}
