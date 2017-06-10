package com.tempvs.item

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * Group of {@link com.tempvs.item.Item} that belong to {@link com.tempvs.item.ItemStash}.
 */
@GrailsCompileStatic
class ItemGroup extends BasePersistent {

    String name
    String description

    static belongsTo = [itemStash: ItemStash]
    static hasMany = [items: Item]

    static constraints = {
        name unique: ['itemStash']
        description nullable: true
    }

    static mapping = {
        itemStash fetch: 'join'
        items batchSize: 20
    }
}