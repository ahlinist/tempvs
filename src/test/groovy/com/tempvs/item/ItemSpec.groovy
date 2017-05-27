package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE_ID = 'itemImageId'
    private static final String SOURCE_IMAGE_ID = 'sourceImageId'

    def itemGroup = Mock(ItemGroup)

    def setup() {
    }

    def cleanup() {
    }

    void "Test item creation being not assigned to itemGroup"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.description = DESCRIPTION


        expect:
        !item.validate()
    }

    void "Test item creation having no name"() {
        given:
        Item item = new Item()
        item.description = DESCRIPTION
        item.itemGroup = itemGroup

        expect:
        !item.validate()
    }

    void "Test correct item creation with minimal data"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.itemGroup = itemGroup

        expect:
        item.validate()
    }

    void "Test correct item creation with maximal data"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.description = DESCRIPTION
        item.itemImageId = ITEM_IMAGE_ID
        item.sourceImageId = SOURCE_IMAGE_ID
        item.itemGroup = itemGroup

        expect:
        item.validate()
    }
}
