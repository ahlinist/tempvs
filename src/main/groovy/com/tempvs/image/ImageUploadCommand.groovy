package com.tempvs.image

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command for {@link com.tempvs.image.Image} objects binding.
 */
@GrailsCompileStatic
class ImageUploadCommand implements Validateable {
    List<ImageUploadBean> imageUploadBeans
}
