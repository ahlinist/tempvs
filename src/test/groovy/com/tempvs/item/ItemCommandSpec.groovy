package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.item.ItemCommand}.
 */
@TestFor(ItemController)
class ItemCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def setup() {

    }

    def cleanup() {

    }

    void "Test ItemCommand"() {
        expect:
        !new ItemCommand().validate()

        and:
        new ItemCommand(name: NAME).validate()

        and:
        new ItemCommand(name: NAME, description: DESCRIPTION).validate()
    }
}
