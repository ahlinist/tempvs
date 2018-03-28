package club.tempvs.item

import club.tempvs.image.Image
import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ItemSpec extends Specification implements DomainUnitTest<Item> {

    private static final String NAME = 'name'
    private static final String SOURCE = 'source'
    private static final String DESCRIPTION = 'description'

    def image = Mock Image
    def source = Mock Source
    def type = GroovyMock Type
    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup
    def item2Source = Mock Item2Source


    def setup() {
        GroovyMock(Item2Source, global: true)
    }

    def cleanup() {
    }

    void "Test item creation being not assigned to itemGroup"() {
        given:
        domain.name = NAME
        domain.description = DESCRIPTION
        domain.period = period

        expect:
        !domain.validate()
    }

    void "Test item creation having no name"() {
        given:
        domain.description = DESCRIPTION
        domain.itemGroup = itemGroup
        domain.period = period

        expect:
        !domain.validate()
    }

    void "Test correct item creation with minimal data"() {
        given:
        domain.name = NAME
        domain.itemGroup = itemGroup
        domain.period = period
        domain.type = type

        expect:
        domain.validate()
    }

    void "Test correct item creation with maximum data"() {
        given:
        domain.name = NAME
        domain.description = DESCRIPTION
        domain.images = [image]
        domain.itemGroup = itemGroup
        domain.period = period
        domain.type = type

        expect:
        domain.validate()
    }

    void "Test item creation without period assigned"() {
        given:
        domain.name = NAME
        domain.itemGroup = itemGroup

        expect:
        !domain.validate()
    }

    void "Test getSources()"() {
        when:
        def result = domain.getSources()

        then:
        1 * Item2Source.findAllByItem(domain) >> [item2Source, item2Source]
        2 * item2Source.getProperty(SOURCE) >> source
        0 * _

        and:
        result == [source, source]
    }
}
