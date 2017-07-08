package com.tempvs.image

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * An entity that includes image's id and info.
 */
@GrailsCompileStatic
class Image extends BasePersistent {

    String objectId
    String imageInfo
    String collection

    static constraints = {
        imageInfo nullable: true
    }
}
