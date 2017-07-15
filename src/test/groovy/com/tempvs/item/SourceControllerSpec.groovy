package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.periodization.Period
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SourceController)
class SourceControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String REFERER = 'referer'
    private static final String PROPERTIES = 'properties'
    private static final String SHOW_URI = '/source/show'
    private static final String PERIOD_URI = '/source/period'

    def json = Mock(JSON)
    def source = Mock(Source)
    def period = Period.XIX
    def sourceService = Mock(SourceService)
    def sourceCommand = Mock(SourceCommand)
    def ajaxResponseService = Mock(AjaxResponseService)

    def setup() {
        controller.sourceService = sourceService
        controller.ajaxResponseService = ajaxResponseService
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        !response.redirectedUrl
        !controller.modelAndView
    }

    void "Test show()"() {
        when:
        def result = controller.show(ID)

        then:
        1 * sourceService.getSource(ID) >> source
        1 * source.period >> period
        0 * _

        and:
        result == [source: source, period: period, editAllowed: Boolean.TRUE]
    }

    void "Test createSource()"() {
        given:
        controller.request.addHeader(REFERER, "${PERIOD_URI}/${period.id}")

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.getProperty(PROPERTIES) >> [:]
        1 * sourceService.createSource(_ as Map) >> source
        1 * source.validate() >> Boolean.TRUE
        1 * source.id >> LONG_ID
        1 * ajaxResponseService.renderRedirect("${SHOW_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test period()"() {
        given:
        params.id = period.id
        List<Source> sources = [source]

        when:
        def result = controller.period()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> sources
        0 * _

        and:
        result == [sources: sources, period: period, editAllowed: Boolean.TRUE]
    }

    void "Test editSource()"() {
        given:
        controller.request.addHeader(REFERER, "${SHOW_URI}/${ID}")

        when:
        controller.editSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.getProperty(PROPERTIES) >> [:]
        1 * sourceService.getSource(ID) >> source
        1 * sourceService.editSource(source, [:]) >> source
        1 * source.validate() >> Boolean.TRUE
        1 * source.id >> LONG_ID
        1 * ajaxResponseService.renderRedirect("${SHOW_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}