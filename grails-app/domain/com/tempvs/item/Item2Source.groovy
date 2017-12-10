package com.tempvs.item

import com.tempvs.domain.BasePersistent

/**
 * An entity that represents relation between {@link com.tempvs.item.Item}
 * and {@link com.tempvs.item.Source}.
 */
class Item2Source extends BasePersistent {

    Item item
    Source source

    static constraints = {
        item unique: ['source']
        source validator: { Source source, Item2Source item2Source ->
            source.period == item2Source.item?.period
        }
    }

    static mapping = {
        id composite: ['item', 'source']
    }
}
