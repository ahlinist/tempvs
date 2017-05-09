package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ItemGroup)
class ItemGroupSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def itemStash = Mock(ItemStash)

    def setup() {
    }

    def cleanup() {
    }

    void "Test itemGroup creation being not assigned to itemStash"() {
        given:
        ItemGroup itemGroup = new ItemGroup()
        itemGroup.name = NAME
        itemGroup.description = DESCRIPTION

        expect:
        !itemGroup.validate()
    }

    void "Test itemGroup creation without name"() {
        given:
        ItemGroup itemGroup = new ItemGroup()
        itemGroup.itemStash = itemStash
        itemGroup.description = DESCRIPTION

        expect:
        !itemGroup.validate()
    }

    void "Test correct itemGroup creation"() {
        given:
        ItemGroup itemGroup = new ItemGroup()
        itemGroup.itemStash = itemStash
        itemGroup.name = NAME

        expect:
        itemGroup.validate()
    }
}
