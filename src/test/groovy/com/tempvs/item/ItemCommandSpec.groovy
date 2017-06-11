package com.tempvs.item

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.item.ItemCommand}.
 */
@TestFor(ItemController)
class ItemCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String ITEM_IMAGE_FILE_NAME = 'itemImage'
    private static final String SOURCE_IMAGE_FILE_NAME = 'sourceImage'

    def setup() {

    }

    def cleanup() {

    }

    void "Test ItemCommand"() {
        given:
        def itemImage = new MockMultipartFile(ITEM_IMAGE_FILE_NAME, "1234567" as byte[])
        def sourceImage = new MockMultipartFile(SOURCE_IMAGE_FILE_NAME, "1234567" as byte[])

        expect:
        !new ItemCommand().validate()

        and:
        new ItemCommand(name: NAME).validate()

        and:
        new ItemCommand(name: NAME, itemImage: itemImage, sourceImage: sourceImage).validate()
    }
}
