package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.converters.JSON
import grails.gsp.PageRenderer
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

    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String NAME = 'name'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String FIELD_NAME = 'fieldName'
    private static final String PROPERTIES = 'properties'
    private static final String SHOW_URI = '/source/show'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String DESCRIPTION = 'description'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String PERIOD_URI = '/source/period'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String APPEND_ACTION = 'appendElement'

    def json = Mock JSON
    def image = Mock Image
    def period = Period.XIX
    def source = Mock Source
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def sourceCommand = Mock SourceCommand
    def imageUploadBean = Mock ImageUploadBean
    def groovyPageRenderer = Mock PageRenderer
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.sourceService = sourceService
        controller.imageService = imageService
        controller.groovyPageRenderer = groovyPageRenderer
        controller.ajaxResponseHelper = ajaxResponseHelper
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
        def result = controller.show(LONG_ONE)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * source.period >> period
        1 * source.images >> [image]
        0 * _

        and:
        result == [source: source, period: period, images: [image]]
    }

    void "Test createSource()"() {
        given:
        request.method = POST_METHOD
        controller.request.addHeader(REFERER, "${PERIOD_URI}/${period.id}")

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.description >> DESCRIPTION
        1 * sourceCommand.errors
        1 * sourceCommand.type
        1 * sourceCommand.period >> period
        1 * sourceCommand.name >> NAME
        2 * sourceCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], SOURCE_COLLECTION) >> [image]
        1 * sourceService.updateSource(_ as Source, [image]) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * source.id >> SourceControllerSpec.LONG_ONE
        1 * ajaxResponseHelper.renderRedirect("${SHOW_URI}/${SourceControllerSpec.LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createSource() against invalid source"() {
        given:
        params.sourceId = ONE
        request.method = POST_METHOD

        when:
        controller.createSource(sourceCommand)

        then:
        1 * sourceCommand.validate() >> Boolean.TRUE
        1 * sourceCommand.description
        1 * sourceCommand.errors
        1 * sourceCommand.type
        1 * sourceCommand.period
        1 * sourceCommand.name
        2 * sourceCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], SOURCE_COLLECTION) >> [image]
        1 * sourceService.updateSource(_ as Source, [image]) >> source
        1 * source.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as Source) >> json
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
        1 * ajaxResponseHelper.renderValidationResponse(sourceCommand) >> json
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
        result == [sources: sources, period: period]
    }

    void "Test getSourcesByPeriod()"() {
        given:
        params.id = period.id

        when:
        controller.getSourcesByPeriod()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> {[source, source]}
        2 * source.id >> SourceControllerSpec.LONG_ONE
        2 * source.name >> NAME
        0 * _
    }

    void "Test editSourceField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editSourceField(LONG_ONE, FIELD_NAME, FIELD_VALUE)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * sourceService.editSourceField(source, FIELD_NAME, FIELD_VALUE) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test addImage()"() {
        given:
        params.sourceId = LONG_ONE
        request.method = POST_METHOD

        when:
        controller.addImage(imageUploadBean)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * imageService.uploadImage(imageUploadBean, SOURCE_COLLECTION) >> image
        1 * sourceService.updateSource(source, [image]) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == APPEND_ACTION
    }

    void "Test deleteSource()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteSource(LONG_ONE)

        then: 'Successfully deleted'
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * sourceService.deleteSource(source)
        0 * _

        and:
        response.json.action == DELETE_ACTION
    }

    void "Test deleteImage()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteImage(LONG_ONE, LONG_TWO)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * imageService.getImage(LONG_TWO) >> image
        1 * sourceService.deleteImage(source, image) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == DELETE_ACTION
    }
}
