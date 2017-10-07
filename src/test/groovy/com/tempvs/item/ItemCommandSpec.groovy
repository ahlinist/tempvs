package com.tempvs.item

import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.item.ItemCommand}.
 */
@TestFor(ItemController)
class ItemCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup

    def setup() {

    }

    def cleanup() {

    }

    void "Test ItemCommand"() {
        expect:
        !new ItemCommand().validate()

        and:
        !new ItemCommand(name: NAME).validate()

        and:
        !new ItemCommand(name: NAME, description: DESCRIPTION).validate()

        and:
        !new ItemCommand(name: NAME, description: DESCRIPTION, period: period).validate()

        and:
        new ItemCommand(name: NAME, description: DESCRIPTION, period: period, itemGroup: itemGroup).validate()
    }
}
