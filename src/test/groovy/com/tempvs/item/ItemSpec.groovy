package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemSpec extends Specification {

    def itemGroup = Mock(ItemGroup)

    def setup() {
    }

    def cleanup() {
    }

    void "Test item creation being not assigned to itemGroup"() {
        expect:
        !new Item().validate()
    }

    void "Test item creation being assigned to itemGroup"() {
        given:
        Item item = new Item()
        item.itemGroup = itemGroup

        expect:
        item.validate()
    }
}
