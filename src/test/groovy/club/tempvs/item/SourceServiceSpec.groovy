package club.tempvs.item

import club.tempvs.image.Image
import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SourceServiceSpec extends Specification implements ServiceUnitTest<SourceService>, DomainUnitTest<Source> {

    def image = Mock Image
    def source = Mock Source
    def itemType = GroovyMock ItemType
    def period = GroovyMock Period

    def setup() {
        GroovySpy(Source, global: true)
    }

    def cleanup() {
    }

    void "Test getSourcesByPeriodAndItemType()"() {
        when:
        def result = service.getSourcesByPeriodAndItemType(period, itemType)

        then:
        1 * Source.withCriteria(_ as Closure)>> [source]
        0 * _

        and:
        result == [source]
    }
}
