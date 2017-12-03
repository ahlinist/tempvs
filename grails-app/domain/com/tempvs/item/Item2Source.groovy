package com.tempvs.item

import com.tempvs.domain.BasePersistent

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
