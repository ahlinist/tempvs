package com.tempvs.item

import com.tempvs.domain.BasePersistent
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

    static mapping = {
        id composite: ['item', 'passport']
    }
}
