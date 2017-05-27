package com.tempvs.item

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds the reconstruted item.
 */
@GrailsCompileStatic
class Item extends BasePersistent {

    String name
    String description
    String itemImageId
    String sourceImageId

    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        description nullable: true
        itemImageId nullable: true
        sourceImageId nullable: true
    }

    static mapping = {
        itemGroup fetch: 'join'
    }
}
