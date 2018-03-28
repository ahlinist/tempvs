package club.tempvs.item

import club.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds the relation of {@link Item} to {@ link Passport}.
 */
@GrailsCompileStatic
class Item2Passport implements BasePersistent {

    Item item
    Passport passport
    Long quantity

    static constraints = {
        quantity min: 1L
        item unique: ['passport']
    }

    int hashCode() {
        item.hashCode() * passport.hashCode() * quantity.hashCode()
    }

    boolean equals(Object obj) {
        Item2Passport object = (Item2Passport) obj

        if (object.item != this.item) {
            return false
        }

        if (object.passport != this.passport) {
            return false
        }

        if (object.quantity != this.quantity) {
            return false
        }

        return true
    }
}
