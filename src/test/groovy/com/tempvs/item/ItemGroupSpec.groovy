package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ItemGroup)
class ItemGroupSpec extends Specification {

    def itemStash = Mock(ItemStash)

    def setup() {
    }

    def cleanup() {
    }

    void "Test itemGroup creation being not assigned to itemStash"() {
        expect:
        !new ItemGroup().validate()
    }

    void "Test itemGroup creation being assigned to itemStash"() {
        given:
        ItemGroup itemGroup = new ItemGroup()
        itemGroup.itemStash = itemStash

        expect:
        itemGroup.validate()
    }
}
