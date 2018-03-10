package com.tempvs.image

import grails.compiler.GrailsCompileStatic
import org.springframework.web.multipart.MultipartFile
import grails.validation.Validateable

/**
 * A bean for uploading {@link org.springframework.web.multipart.MultipartFile} collections with their short descriptions.
 */
@GrailsCompileStatic
class ImageUploadBean implements Validateable {

    private static final List<String> CONTENT_TYPES = ['image/png', 'image/jpeg', 'image/jpg', 'image/gif']

    MultipartFile image
    String imageInfo

    static constraints = {
        imageInfo nullable: true, size: 0..255
        image validator: { MultipartFile image ->
            !image.empty ? image.contentType in CONTENT_TYPES : Boolean.TRUE
        }
    }
}
