package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

/**
 * Unit-test suite for testing {@link com.tempvs.item.Source}-related views.
 */
@TestMixin(GroovyPageUnitTestMixin)
class SourceViewSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String NAME = 'name'
    private static final String VALUE = 'value'
    private static final String PERIOD = 'period'
    private static final String SHOW_URI = '/source/show'
    private static final String DESCRIPTION = 'description'

    def image = Mock(Image)
    def period = Period.XIX
    def source = Mock(Source)

    def setup() {

    }

    def cleanup() {

    }

    void "Test /source/show"() {
        given:
        String title = "<title>Tempvs - ${NAME}</title>"
        String editForm = '<tempvs:modalButton id="sourceForm" message="source.editSource.button">'
        Map model = [source: source, period: period, editAllowed: Boolean.TRUE]

        when:
        String result = render view: '/source/show', model: model

        then:
        4 * source.getProperty(NAME) >> NAME
        2 * source.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * source.getProperty(ID) >> ID
        0 * _

        and:
        result.contains editForm
        result.contains title
    }

    void "Test /source/index"() {
        given:
        String periodLink = '<a href="/test/period/ancient" class="btn btn-default">'

        when:
        String result = render view: '/source/index'

        then:
        result.contains periodLink
    }

    void "Test /source/period"() {
        given:
        String createForm = '<tempvs:modalButton id="sourceForm" message="source.createSource.button">'
        Map model = [sources: [source], period: period, editAllowed: Boolean.TRUE]

        when:
        String result = render view: "/source/period", model: model

        then:
        1 * source.getProperty(NAME) >> NAME
        1 * source.getProperty(ID) >> LONG_ID
        0 * _

        and:
        result.contains createForm
        result.contains SHOW_URI
    }
}
