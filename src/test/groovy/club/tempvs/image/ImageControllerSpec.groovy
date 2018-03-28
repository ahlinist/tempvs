package club.tempvs.image

import asset.pipeline.grails.AssetResourceLocator
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import org.springframework.core.io.Resource

class ImageControllerSpec extends Specification implements ControllerUnitTest<ImageController> {

    private static final String ID = 'id'
    private static final String COLLECTION = 'collection'
    private static final String DEFAULT_IMAGE = 'default_image.gif'

    def resource = Mock(Resource)
    def imageService = Mock(ImageService)
    def assetResourceLocator = Mock(AssetResourceLocator)

    def setup() {
        controller.imageService = imageService
        controller.assetResourceLocator = assetResourceLocator
    }

    def cleanup() {
    }

    void "Test get() action"() {
        given:
        params.id = ID
        params.collection = COLLECTION

        when:
        controller.get()

        then:
        1 * imageService.getImageBytes(COLLECTION, ID)
        1 * assetResourceLocator.findAssetForURI(DEFAULT_IMAGE) >> resource
        1 * resource.inputStream
        0 * _
    }
}
