package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SourceServiceSpec extends Specification implements ServiceUnitTest<SourceService>, DomainUnitTest<Source> {

    private static final Long LONG_ONE = 1L
    private static final String NAME = 'name'
    private static final String FIELD_VALUE = 'fieldValue'

    def image = Mock Image
    def source = Mock Source
    def type = GroovyMock Type
    def period = GroovyMock Period
    def item2Source = Mock Item2Source
    def imageService = Mock ImageService

    def setup() {
        service.imageService = imageService

        GroovySpy(Source, global: true)
        GroovySpy(Item2Source, global: true)
    }

    def cleanup() {
    }

    void "Test getSource()"() {
        when:
        def result = service.getSource(LONG_ONE)

        then:
        1 * Source.get(LONG_ONE) >> source
        0 * _

        and:
        result == source
    }

    void "Test getSourcesByPeriod()"() {
        when:
        def result = service.getSourcesByPeriod(period)

        then:
        1 * Source.findAllByPeriod(period) >> [source]
        0 * _

        and:
        result == [source]
    }

    void "Test getSourcesByPeriodAndType()"() {
        when:
        def result = service.getSourcesByPeriodAndType(period, type)

        then:
        1 * Source.findAllByPeriodAndType(period, type) >> [source]
        0 * _

        and:
        result == [source]
    }

    void "Test editSourceField()"() {
        when:
        def result = service.editSourceField(source, NAME, FIELD_VALUE)

        then:
        1 * source.setName(FIELD_VALUE)
        1 * source.save() >> source
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
        1 * source.save() >> source
        0 * _

        and:
        result == source
    }

    void "Test deleteSource()"() {
        when:
        service.deleteSource(source)

        then:
        1 * Item2Source.findAllBySource(source) >> [item2Source, item2Source]
        2 * item2Source.delete()
        1 * source.images >> [image]
        1 * imageService.deleteImages([image])
        1 * source.delete()
        0 * _
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImage(source, image)

        then:
        1 * source.images >> [image]
        1 * source.removeFromImages(image)
        1 * imageService.deleteImage(image)
        1 * source.save() >> source
        0 * _

        and:
        result == source
    }
}
