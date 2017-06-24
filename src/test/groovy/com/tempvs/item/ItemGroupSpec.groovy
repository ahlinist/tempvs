package com.tempvs.item

import com.tempvs.user.User
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ItemGroup)
class ItemGroupSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def user = Mock(User)

    def setup() {
    }

    def cleanup() {
    }

    void "Test itemGroup creation being not assigned to user"() {
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
        itemGroup.user = user
        itemGroup.description = DESCRIPTION

        expect:
        !itemGroup.validate()
    }

    void "Test correct itemGroup creation"() {
        given:
        ItemGroup itemGroup = new ItemGroup()
        itemGroup.user = user
        itemGroup.name = NAME

        expect:
        itemGroup.validate()
    }
}
