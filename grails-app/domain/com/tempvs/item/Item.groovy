package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period

/**
 * An entity that corresponds the reconstructed item.
 */
class Item implements BasePersistent {

    String name
    String description
    Type type
    Period period
    Collection<Image> images

    List<Source> getSources() {
        Item2Source.findAllByItem(this)*.source
    }

    static hasMany = [images: Image]
    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        name blank: false
        description nullable: true
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
    }
}
