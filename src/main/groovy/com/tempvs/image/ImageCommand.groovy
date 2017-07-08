package com.tempvs.image

import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile

/**
 * Command for {@link com.tempvs.image.Image} objects binding.
 */
class ImageCommand implements Validateable {

    MultipartFile image
    String imageInfo

    static constraints = {
        imageInfo nullable: true
    }
}
