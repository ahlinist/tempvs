package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds the reconstruted item.
 */
@GrailsCompileStatic
class Item extends BasePersistent {

    String name
    String description
    Image itemImage
    Image sourceImage

    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        description nullable: true
        itemImage nullable: true
        sourceImage nullable: true
    }

    static mapping = {
        itemGroup fetch: 'join'
        itemImage cascade: 'all-delete-orphan'
        sourceImage cascade: 'all-delete-orphan'
    }
}
