package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SourceService)
class SourceServiceSpec extends Specification {

    private static final String ID = 'id'

    def image = Mock(Image)
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

    void "Test getSourcesByPeriod()"() {
        given:
        List<Source> sources = [source]
        Map params = [period: period]

        when:
        def result = service.getSourcesByPeriod(period)

        then:
        1 * objectDAO.findAll(Source, params) >> sources
        0 * _

        and:
        result == sources
    }

    void "Test createSource()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.createSource(source, images)

        then:
        2 * source.addToImages(image)
        1 * source.save() >> source
        0 * _

        and:
        result == source
    }

    void "Test editSource()"() {
        given:
        Map params = [:]

        when:
        def result = service.editSource(source, params)

        then:
        1 * source.save() >> source
        0 * _

        and:
        result == source
    }
}
