package com.tempvs.image

import com.tempvs.ajax.AjaxResponseService
import grails.compiler.GrailsCompileStatic
import org.springframework.util.StreamUtils
import asset.pipeline.grails.AssetResourceLocator

/**
 * Controller for {@link com.tempvs.image.Image} handling. 
 */
@GrailsCompileStatic
class ImageController {

    private static final String DEFAULT_IMAGE = 'defaultImage.jpg'

    ImageService imageService
    AssetResourceLocator assetResourceLocator
    AjaxResponseService ajaxResponseService

    def get(String id) {
        String collection = params.collection
        byte[] imageInBytes = imageService.getImage(collection, id)?.bytes ?:
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
