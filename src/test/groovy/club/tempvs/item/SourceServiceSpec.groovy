package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SourceServiceSpec extends Specification implements ServiceUnitTest<SourceService>, DomainUnitTest<Source> {

    def image = Mock Image
    def source = Mock Source
    def itemType = GroovyMock ItemType
    def comment = Mock Comment
    def period = GroovyMock Period

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

    void "Test getSourcesByPeriodAndItemType()"() {
        when:
        def result = service.getSourcesByPeriodAndItemType(period, itemType)

        then:
        1 * Source.withCriteria(_ as Closure)>> [source]
        0 * _

        and:
        result == [source]
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
