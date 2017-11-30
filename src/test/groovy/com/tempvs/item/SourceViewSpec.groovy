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
    private static final String IMAGES = 'images'
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
        Set<Image> images = [image] as Set
        String title = "<title>Tempvs - ${NAME}</title>"
        Map model = [source: source, period: period, editAllowed: Boolean.TRUE]

        when:
        String result = render view: '/source/show', model: model

        then:
        3 * source.getProperty(NAME) >> NAME
        1 * source.getProperty(DESCRIPTION) >> DESCRIPTION
        2 * source.getProperty(ID) >> ID
        1 * source.getProperty(PERIOD) >> period
        0 * _

        and:
        result.contains title
    }

    void "Test /source/index"() {
        given:
        String periodLink = '<a href="/test/period/ancient" class="btn btn-default col-sm-3">'

        when:
        String result = render view: '/source/index'

        then:
        0 * _

        and:
        result.contains periodLink
    }

    void "Test /source/period"() {
        given:
        String createForm = '<tempvs:modalButton id="sourceForm" classes="glyphicon glyphicon-plus">'
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
