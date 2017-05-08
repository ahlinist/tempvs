package com.tempvs.item

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * Group of {@link com.tempvs.item.Item} that belong to {@link com.tempvs.item.ItemStash}.
 */
@GrailsCompileStatic
class ItemGroup extends BasePersistent {

    static belongsTo = [itemStash: ItemStash]
    static hasMany = [items: Item]

    static constraints = {
    }
}
