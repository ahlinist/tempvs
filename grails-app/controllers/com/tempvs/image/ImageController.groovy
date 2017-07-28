package com.tempvs.image

import asset.pipeline.grails.AssetResourceLocator
import grails.compiler.GrailsCompileStatic

/**
 * Controller for {@link com.tempvs.image.Image} handling.
 */
@GrailsCompileStatic
class ImageController {

    private static final String DEFAULT_IMAGE = 'defaultImage.jpg'

    ImageService imageService
    AssetResourceLocator assetResourceLocator

    def get() {
        byte[] imageInBytes = imageService.getImageBytes(params.collection as String, params.id as String) ?:
                assetResourceLocator.findAssetForURI(DEFAULT_IMAGE)?.getInputStream()?.bytes ?: new byte[0]

        response.with{
            setHeader('Content-length', imageInBytes.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInBytes
            outputStream.flush()
        }
    }
}
