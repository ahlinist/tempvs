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
    private static final String SOURCE = 'source'
    private static final String DESCRIPTION = 'description'

    def image = Mock Image
    def period = Period.XIX
    def source = Mock Source
    def itemGroup = Mock ItemGroup
    def item2Source = Mock Item2Source

    Item item

    def setup() {
        GroovySpy(Item2Source, global: true)

        item = new Item()
    }

    def cleanup() {
    }

    void "Test item creation being not assigned to itemGroup"() {
        given:
        item.name = NAME
        item.description = DESCRIPTION
        item.period = period

        expect:
        !item.validate()
    }

    void "Test item creation having no name"() {
        given:
        item.description = DESCRIPTION
        item.itemGroup = itemGroup
        item.period = period

        expect:
        !item.validate()
    }

    void "Test correct item creation with minimal data"() {
        given:
        item.name = NAME
        item.itemGroup = itemGroup
        item.period = period

        expect:
        item.validate()
    }

    void "Test correct item creation with maximum data"() {
        given:
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
        item.name = NAME
        item.itemGroup = itemGroup

        expect:
        !item.validate()
    }

    void "Test getSources()"() {
        when:
        def result = item.getSources()

        then:
        1 * Item2Source.findAllByItem(_ as Item, [sort: "id"]) >> [item2Source]
        1 * item2Source.getProperty(SOURCE) >> source
        0 * _

        and:
        result == [source]
    }
}
