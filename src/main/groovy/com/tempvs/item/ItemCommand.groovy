package com.tempvs.item

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for edition or creation of new {@link com.tempvs.item.Item} instance.
 */
@GrailsCompileStatic
class ItemCommand implements Validateable {

    String name
    String description
    Source source
    Type type
    Period period
    ItemGroup itemGroup
    List<ImageUploadBean> imageUploadBeans

    static constraints = {
        description nullable: true
        source nullable: true
    }
}
