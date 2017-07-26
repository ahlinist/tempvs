package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item)
class ItemSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def image = Mock(Image)
    def itemGroup = Mock(ItemGroup)
    def period = GroovyMock(Period)

    def setup() {
    }

    def cleanup() {
    }

    void "Test item creation being not assigned to itemGroup"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.description = DESCRIPTION
        item.period = period

        expect:
        !item.validate()
    }

    void "Test item creation having no name"() {
        given:
        Item item = new Item()
        item.description = DESCRIPTION
        item.itemGroup = itemGroup
        item.period = period

        expect:
        !item.validate()
    }

    void "Test correct item creation with minimal data"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.itemGroup = itemGroup
        item.period = period

        expect:
        item.validate()
    }

    void "Test correct item creation with maximum data"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.description = DESCRIPTION
        item.images = [image] as Set
        item.itemGroup = itemGroup
        item.period = period

        expect:
        item.validate()
    }

    void "Test item creation without period assigned"() {
        given:
        Item item = new Item()
        item.name = NAME
        item.itemGroup = itemGroup

        expect:
        !item.validate()
    }
}
