package com.tempvs.item

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for creation of new {@link com.tempvs.item.Source} instance.
 */
@GrailsCompileStatic
class SourceCommand implements Validateable {

    String name
    String description
    Period period
    List<ImageUploadBean> imageUploadBeans

    static constraints = {
        description nullable: true
    }
}
