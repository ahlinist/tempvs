package club.tempvs.library

import club.tempvs.periodization.Period
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class LibraryControllerSpec extends Specification implements ControllerUnitTest<LibraryController>, DataTest {

    private static final String GET_METHOD = 'GET'

    def period = Period.OTHER

    def setup() {

    }

    def cleanup() {
    }

    void "Test index()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.index()

        then:
        0 * _

        and:
        !response.redirectedUrl
        !controller.modelAndView
    }

    void "Test period()"() {
        given:
        params.id = period.id
        request.method = GET_METHOD

        when:
        def result = controller.period()

        then:
        0 * _

        and:
        result == [period: period]
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
