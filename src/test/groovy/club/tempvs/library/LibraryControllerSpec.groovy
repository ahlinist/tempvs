package club.tempvs.library

import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class LibraryControllerSpec extends Specification implements ControllerUnitTest<LibraryController> {

    private static final String GET_METHOD = 'GET'

    def source = Mock Source
    def period = Period.OTHER

    def sourceService = Mock SourceService

    def setup() {
        controller.sourceService = sourceService
    }

    def cleanup() {
    }

    void "Test index()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.index()

        then:
        !response.redirectedUrl
        !controller.modelAndView

        and:
        result == [periods: Period.values()]
    }

    void "Test period()"() {
        given:
        params.id = period.id
        request.method = GET_METHOD
        List<Source> sources = [source]

        when:
        def result = controller.period()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> sources
        0 * _

        and:
        result == [sources: sources, period: period, itemTypes: ItemType.values(), sourceTypes: SourceType.values()]
    }
}
