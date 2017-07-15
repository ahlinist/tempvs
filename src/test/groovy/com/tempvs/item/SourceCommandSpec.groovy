package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.item.SourceCommand}.
 */
@TestFor(SourceController)
class SourceCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def setup() {

    }

    def cleanup() {

    }

    void "Test ItemCommand"() {
        expect:
        !new SourceCommand().validate()

        and:
        new SourceCommand(name: NAME).validate()

        and:
        new SourceCommand(name: NAME, description: DESCRIPTION).validate()
    }
}
