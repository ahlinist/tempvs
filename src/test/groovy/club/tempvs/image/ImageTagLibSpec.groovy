package club.tempvs.image

import grails.gsp.PageRenderer
import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class ImageTagLibSpec extends Specification implements TagLibUnitTest<ImageTagLib> {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'

    def image = Mock(Image)
    def groovyPageRenderer = Mock PageRenderer

    def setup() {
        tagLib.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {

    }

    void "Test image()"() {
        when:
        tagLib.image([image: image])

        then:
        1 * image.asType(Image) >> image
        2 * image.collection >> COLLECTION
        1 * image.objectId >> ID
        1 * image.imageInfo >> IMAGE_INFO
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }

    void "Test modalCarousel()"() {
        when:
        tagLib.modalCarousel([images: [image]])

        then:
        1 * image.id >> LONG_ID
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }
}
