package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit-test suite for ItemGroupCommand.
 */
@TestFor(ItemController)
class ItemGroupCommandSpec extends Specification {
    private static final String NAME = 'name'

    def setup() {

    }

    def cleanup() {

    }

    void "Test CreateItemGroupCommand"() {
        expect:
        !new ItemGroupCommand().validate()
        new ItemGroupCommand(name: NAME).validate()
    }
}
