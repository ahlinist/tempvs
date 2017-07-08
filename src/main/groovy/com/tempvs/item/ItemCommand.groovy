package com.tempvs.item

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for creation of new {@link com.tempvs.item.Item} instance.
 */
@GrailsCompileStatic
class ItemCommand implements Validateable {
    String name
    String description

    static constraints = {
        description nullable: true
    }
}
