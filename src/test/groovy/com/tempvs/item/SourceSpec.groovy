package com.tempvs.item

import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Source)
class SourceSpec extends Specification {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def setup() {
    }

    def cleanup() {
    }

    void "Test unsuccessful Source creation"() {
        expect:
        !new Source().validate()

        and:
        !new Source(name: NAME).validate()

        and:
        !new Source(period: Period.ANCIENT).validate()
    }

    void "Test successful Source creation"() {
        expect:
        new Source(name: NAME, period: Period.ANTIQUITY).validate()

        and:
        new Source(name: NAME,  period: Period.ANTIQUITY, description: DESCRIPTION).validate()
    }
}
