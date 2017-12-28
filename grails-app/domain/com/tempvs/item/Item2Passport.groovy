package com.tempvs.item

import com.tempvs.domain.BasePersistent

/**
 * An entity that represents a many-to-many relation between {@link com.tempvs.item.Item} and
 * {@link com.tempvs.item.Passport}.
 */
class Item2Passport extends BasePersistent {

    Item item
    Passport passport
    Long quantity

    static constraints = {
        quantity min: 1L
        item unique: ['passport']
    }

    static mapping = {
        id composite: ['item', 'passport']
    }
}
