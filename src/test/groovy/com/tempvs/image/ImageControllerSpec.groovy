package com.tempvs.image

import asset.pipeline.grails.AssetResourceLocator
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ImageController)
class ImageControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final String COLLECTION = 'collection'
    private static final List<Byte> BYTE_LIST = "test data".bytes

    def imageService = Mock(ImageService)
    def assetResourceLocator = Mock(AssetResourceLocator)
    def image = Mock(Image)

    def setup() {
        controller.imageService = imageService
        controller.assetResourceLocator = assetResourceLocator
    }

    def cleanup() {
    }

    void "Test get() action"() {
        when:
        params.id = ID
        params.collection = COLLECTION
        controller.get()

        then:
        1 * imageService.getImage(COLLECTION, ID) >> image
        1 * image.bytes >> BYTE_LIST
        0 * _
    }
}
