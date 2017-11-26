package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock([Source])
@TestFor(SourceService)
class SourceServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String NAME = 'name'
    private static final String FIELD_VALUE = 'fieldValue'

    def image = Mock Image
    def source = Mock Source
    def period = Period.MEDIEVAL
    def objectDAOService = Mock ObjectDAOService

    def setup() {
        service.objectDAOService = objectDAOService

        GroovySpy(Source, global: true)
    }

    def cleanup() {
    }

    void "Test getSource()"() {
        when:
        def result = service.getSource(ID)

        then:
        1 * objectDAOService.get(Source, ID) >> source
        0 * _

        and:
        result == source
    }

    void "Test getSourcesByPeriod()"() {
        given:
        List<Source> sources = [source]

        when:
        def result = service.getSourcesByPeriod(period)

        then:
        1 * Source.findAllByPeriod(period) >> sources
        0 * _

        and:
        result == sources
    }

    void "Test editSourceField()"() {
        when:
        def result = service.editSourceField(source, NAME, FIELD_VALUE)

        then:
        1 * source.setName(FIELD_VALUE)
        1 * objectDAOService.save(source) >> source
        0 * _

        and:
        result == source
    }

    void "Test updateSource()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.updateSource(source, images)

        then:
        2 * source.addToImages(image)
        1 * objectDAOService.save(source) >> source
        0 * _

        and:
        result == source
    }
}
