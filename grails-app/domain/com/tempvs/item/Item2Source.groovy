package com.tempvs.item

import com.tempvs.domain.BasePersistent

class Item2Source implements BasePersistent {

    Item item
    Source source

    static constraints = {
        source unique: ['item'], validator: { Source source, Item2Source item2Source ->
            source.type == item2Source.item.type
        }
        item validator: { Item item, Item2Source item2Source ->
            item.period == item2Source.source.period
        }
    }

    static mapping = {
        id composite: ['item', 'source']
    }
}
