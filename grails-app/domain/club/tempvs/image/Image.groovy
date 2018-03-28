package club.tempvs.image

import club.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * An entity that includes image's id and info.
 */
@GrailsCompileStatic
class Image implements BasePersistent {

    String objectId
    String imageInfo
    String collection

    static constraints = {
        imageInfo nullable: true, size: 0..255
    }
}
