package com.tempvs.image

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command for {@link com.tempvs.image.Image} objects binding.
 */
@GrailsCompileStatic
class ImageUploadCommand implements Validateable {

    Long id
    String className
    ImageUploadBean imageUploadBean

    static constraints = {
        className nullable: true
        id nullable: true
    }
}
