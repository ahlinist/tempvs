package com.tempvs.item

import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for creation of new {@link com.tempvs.item.ItemGroup} instance.
 */
@GrailsCompileStatic
class ItemGroupCommand implements Validateable {

    String name
    String description
    User user

    static constraints = {
        description nullable: true
    }
}
