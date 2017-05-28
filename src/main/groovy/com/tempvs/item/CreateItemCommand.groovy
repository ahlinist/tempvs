package com.tempvs.item

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile

/**
 * Command object used for creation of new {@link com.tempvs.item.Item} instance.
 */
@GrailsCompileStatic
class CreateItemCommand implements Validateable {
    String name
    String description
    MultipartFile itemImage
    MultipartFile sourceImage

    static constraints = {
        description nullable: true
        itemImage nullable: true
        sourceImage nullable: true
    }
}
