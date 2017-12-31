package com.tempvs.item

import com.tempvs.user.User
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ItemGroupSpec extends Specification implements DomainUnitTest<ItemGroup> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def user = Mock User
    def item = Mock Item

    ItemGroup itemGroup

    def setup() {
        GroovySpy(Item, global: true)

        itemGroup = new ItemGroup()
    }

    def cleanup() {
    }

    void "Test itemGroup creation being not assigned to user"() {
        given:
        itemGroup.name = NAME
        itemGroup.description = DESCRIPTION

        expect:
        !itemGroup.validate()
    }

    void "Test itemGroup creation without name"() {
        given:
        itemGroup.user = user
        itemGroup.description = DESCRIPTION

        expect:
        !itemGroup.validate()
    }

    void "Test correct itemGroup creation"() {
        given:
        itemGroup.user = user
        itemGroup.name = NAME

        expect:
        itemGroup.validate()
    }
}
