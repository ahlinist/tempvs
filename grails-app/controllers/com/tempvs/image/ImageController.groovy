package com.tempvs.image

import asset.pipeline.grails.AssetResourceLocator
import grails.compiler.GrailsCompileStatic
import org.springframework.util.StreamUtils

/**
 * Controller for {@link com.tempvs.image.ImageBean} handling.
 */
@GrailsCompileStatic
class ImageController {

    private static final String DEFAULT_IMAGE = 'defaultImage.jpg'

    ImageService imageService
    AssetResourceLocator assetResourceLocator

    def get(String id) {
        String collection = params.collection
        byte[] imageInBytes = imageService.getImageBean(collection, id)?.bytes ?:
                assetResourceLocator.findAssetForURI(DEFAULT_IMAGE)?.getInputStream()?.bytes ?:
                        StreamUtils.emptyInput().bytes

        response.with{
            setHeader('Content-length', imageInBytes.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInBytes
            outputStream.flush()
        }
    }
}
