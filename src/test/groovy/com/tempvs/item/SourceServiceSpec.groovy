package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
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
    private static final String SOURCE_COLLECTION = 'source'

    def image = Mock(Image)
    def source = Mock(Source)
    def period = Period.MEDIEVAL
    def objectDAO = Mock(ObjectDAO)
    def imageService = Mock(ImageService)
    def objectFactory = Mock(ObjectFactory)
    def imageUploadBean = Mock(ImageUploadBean)

    def setup() {
        service.objectDAO = objectDAO
        service.objectFactory = objectFactory
        service.imageService = imageService
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
        Set<Image> images = [image] as Set
        List<ImageUploadBean> imageUploadBeans = [imageUploadBean]
        Map params = [name: NAME, period: period, imageBeans: imageUploadBeans]

        when:
        def result = service.createSource(params)

        then:
        1 * objectFactory.create(Source) >> source
        1 * imageService.updateImages(imageUploadBeans, SOURCE_COLLECTION) >> images
        1 * source.setImages(images)
        1 * source.setName(NAME)
        1 * source.setPeriod(period)
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
