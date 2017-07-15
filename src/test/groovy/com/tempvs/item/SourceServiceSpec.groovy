package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SourceService)
class SourceServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String NAME = 'name'

    def source = Mock(Source)
    def period = Period.MEDIEVAL
    def objectDAO = Mock(ObjectDAO)
    def objectFactory = Mock(ObjectFactory)

    def setup() {
        service.objectDAO = objectDAO
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test getSource()"() {
        when:
        def result = service.getSource(ID)

        then:
        1 * objectDAO.get(Source, ID) >> source
        0 * _

        and:
        result == source
    }

    void "Test createSource()"() {
        given:
        Map params = [name: NAME, period: period]

        when:
        def result = service.createSource(params)

        then:
        1 * objectFactory.create(Source) >> source
        1 * source.setName(NAME)
        1 * source.setPeriod(period)
        1 * source.save() >> source
        0 * _

        and:
        result == source
    }

    void "Test getSourcesByPeriod()"() {
        given:
        List<Source> sources = [source]
        Map params = [period: period]

        when:
        def result = service.getSourcesByPeriod(period)

        then:
        1 * objectDAO.findAll(Source, params) >> sources
        0 * _
    }
}
