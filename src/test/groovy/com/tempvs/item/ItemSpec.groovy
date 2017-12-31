package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ItemSpec extends Specification implements DomainUnitTest<Item> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def image = Mock Image
    def type = GroovyMock Type
    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup


    def setup() {
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
}
