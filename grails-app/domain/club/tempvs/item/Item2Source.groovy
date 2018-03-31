package club.tempvs.item

import club.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * Defines a relation between {@link Item} and {@link Source}.
 */
@GrailsCompileStatic
class Item2Source implements BasePersistent {

    Item item
    Source source

    static constraints = {
        source unique: ['item'], validator: { Source source, Item2Source item2Source ->
            source.itemType == item2Source.item.itemType
        }
        item validator: { Item item, Item2Source item2Source ->
            item.period == item2Source.source.period
        }
    }

    static mapping = {
        id composite: ['item', 'source']
    }

    int hashCode() {
        item.hashCode() * source.hashCode()
    }

    boolean equals(Object obj) {
        Item2Source object = (Item2Source) obj

        if (object.item != this.item) {
            return false
        }

        if (object.source != this.source) {
            return false
        }

        return true
    }
}
