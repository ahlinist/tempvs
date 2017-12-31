package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period

/**
 * An entity that corresponds the reconstructed item.
 */
class Item extends BasePersistent {

    String name
    String description
    Type type
    Period period
    Collection<Image> images

    static hasMany = [sources: Source, images: Image]
    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        name blank: false
        description nullable: true
        sources validator: { Set sources, Item item ->
             sources*.type.every {it == item.type} && sources*.period.every {it == item.period}
        }
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
    }
}
