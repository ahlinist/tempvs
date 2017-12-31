package com.tempvs.item

import com.tempvs.periodization.Period
import spock.lang.Specification

class ItemCommandSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def type = GroovyMock Type
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
        !new ItemCommand(name: NAME, description: DESCRIPTION, period: period, itemGroup: itemGroup).validate()

        and:
        new ItemCommand(name: NAME, description: DESCRIPTION, period: period, itemGroup: itemGroup, type: type).validate()
    }
}
