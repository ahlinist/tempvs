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

    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        description nullable: true
    }
}
