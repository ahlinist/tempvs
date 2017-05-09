package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit-test suite for CreateItemGroupCommand.
 */
@TestFor(ItemController)
class CreateItemGroupCommandSpec extends Specification {
    private static final String NAME = 'name'

    def setup() {

    }

    def cleanup() {

    }

    void "Test CreateItemGroupCommand"() {
        expect:
        !new CreateItemGroupCommand().validate()
        new CreateItemGroupCommand(name: NAME).validate()
    }
}
