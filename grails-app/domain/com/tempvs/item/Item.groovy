package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds the reconstruted item.
 */
@GrailsCompileStatic
class Item extends BasePersistent {

    String name
    String description
    Period period
    Source source

    static hasMany = [images: Image]
    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        name blank: false
        description nullable: true
        images nullable: true
        source nullable: true, validator: { Source source, Item item ->
            source ? source?.period == item.period : Boolean.TRUE
        }
    }

    static mapping = {
        period fetch: 'join'
        itemGroup fetch: 'join'
        images cascade: 'all-delete-orphan'
    }
}
