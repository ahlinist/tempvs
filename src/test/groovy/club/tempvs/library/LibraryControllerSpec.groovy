package club.tempvs.library

import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.gsp.PageRenderer
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class LibraryControllerSpec extends Specification implements ControllerUnitTest<LibraryController>, DataTest {

    private static final String GET_METHOD = 'GET'

    def user = Mock User
    def source = Mock Source
    def period = Period.OTHER

    def userService = Mock UserService
    def sourceService = Mock SourceService
    def groovyPageRenderer = Mock PageRenderer

    def setup() {
        controller.userService = userService
        controller.sourceService = sourceService
        controller.groovyPageRenderer = groovyPageRenderer

        GroovySpy(Role, global:true)
    }

    def cleanup() {
    }

    void "Test index()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.index()

        then:
        0 * _

        and:
        !response.redirectedUrl
        !controller.modelAndView

        result == [
                periods: Period.values()
        ]
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

    void "Test admin()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.admin()

        then:
        !response.redirectedUrl
        !controller.modelAndView
    }
}
