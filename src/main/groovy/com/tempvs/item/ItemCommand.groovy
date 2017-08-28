package com.tempvs.item

import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * Command object used for edition or creation of new {@link com.tempvs.item.Item} instance.
 */
@GrailsCompileStatic
class ItemCommand extends BaseItemCommand {

    String name
    String description
    Source source
    Period period

    static constraints = {
        description nullable: true
        source nullable: true
    }
}
