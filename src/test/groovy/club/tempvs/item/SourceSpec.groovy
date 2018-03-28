package club.tempvs.item

import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SourceSpec extends Specification implements DomainUnitTest<Source> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def type = GroovyMock Type
    def period = GroovyMock Period

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
        new Source(name: NAME, period: period, type: type).validate()

        and:
        new Source(name: NAME,  period: period, type: type, description: DESCRIPTION).validate()
    }
}
