package com.tempvs.image

import asset.pipeline.grails.AssetResourceLocator
import grails.test.mixin.TestFor
import spock.lang.Specification
import org.springframework.core.io.Resource

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ImageController)
class ImageControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final String COLLECTION = 'collection'
    private static final String DEFAULT_IMAGE = 'defaultImage.jpg'

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
