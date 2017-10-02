package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([Source])
@TestFor(SourceController)
class SourceControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final Long LONG_ID = 1L
    private static final String NAME = 'name'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String OBJECT_ID = 'objectId'
    private static final String FIELD_NAME = 'fieldName'
    private static final String PROPERTIES = 'properties'
    private static final String SHOW_URI = '/source/show'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String DESCRIPTION = 'description'

    private static final String PERIOD_URI = '/source/period'
    private static final String NO_SOURCE_FOUND = 'source.notFound.message'

    def json = Mock JSON
    def period = Period.XIX
    def source = Mock Source
    def sourceService = Mock SourceService
    def sourceCommand = Mock SourceCommand
    def imageUploadBean = Mock ImageUploadBean
    def ajaxResponseService = Mock AjaxResponseService

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
        request.method = POST_METHOD
        controller.request.addHeader(REFERER, "${PERIOD_URI}/${period.id}")
        Map properties = [name: NAME, description: DESCRIPTION, period: period, imageUploadBeans: [imageUploadBean]]

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.getProperty(PROPERTIES) >> properties
        1 * sourceCommand.imageUploadBeans >> [imageUploadBean]
        1 * sourceService.createSource(_ as Source, [imageUploadBean]) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseService.renderRedirect("${PERIOD_URI}/${period.id}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createSource() against invalid source"() {
        given:
        params.sourceId = ONE
        request.method = POST_METHOD
        Map properties = [imageUploadBeans: [imageUploadBean]]

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.getProperty(PROPERTIES) >> properties
        1 * sourceCommand.imageUploadBeans >> [imageUploadBean]
        1 * sourceService.createSource(_ as Source, [imageUploadBean]) >> source
        1 * source.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.renderValidationResponse(_ as Source) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createSource() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(sourceCommand) >> json
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

    void "Test getSourcesByPeriod()"() {
        given:
        params.id = period.id

        when:
        controller.getSourcesByPeriod()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> {[source, source]}
        2 * source.id >> LONG_ID
        2 * source.name >> NAME
        0 * _
    }

    void "Test editSourceField()"() {
        given:
        request.method = POST_METHOD
        params.objectId = OBJECT_ID
        params.fieldName = FIELD_NAME
        params.fieldValue = FIELD_VALUE

        when:
        controller.editSourceField()

        then:
        1 * sourceService.getSource(OBJECT_ID) >> source
        1 * sourceService.editSourceField(source, FIELD_NAME, FIELD_VALUE) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.success == Boolean.TRUE
    }
}
