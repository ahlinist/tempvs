package club.tempvs.item

import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SourceSpec extends Specification implements DomainUnitTest<Source> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def period = GroovyMock Period
    def itemType = GroovyMock ItemType
    def sourceType = GroovyMock SourceType

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
        new Source(name: NAME, period: period, itemType: itemType, sourceType: sourceType).validate()

        and:
        new Source(name: NAME,  period: period, itemType: itemType, sourceType: sourceType, description: DESCRIPTION).validate()
    }
}
