package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SourceServiceSpec extends Specification implements ServiceUnitTest<SourceService>, DomainUnitTest<Source> {

    private static final String NAME = 'name'
    private static final String FIELD_VALUE = 'fieldValue'

    def image = Mock Image
    def source = Mock Source
    def type = GroovyMock Type
    def comment = Mock Comment
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

    void "Test saveSource()"() {
        when:
        def result = service.saveSource(source)

        then:
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

    void "Test addComment()"() {
        when:
        def result = service.addComment(source, comment)

        then:
        1 * source.addToComments(comment) >> source
        1 * source.save()
        0 * _

        and:
        result == source
    }

    void "Test deleteComment()"() {
        when:
        def result = service.deleteComment(source, comment)

        then:
        1 * source.removeFromComments(comment) >> source
        1 * source.save()
        0 * _

        and:
        result == source
    }
}
