package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic

/**
 * Group of {@link com.tempvs.item.Item} that belong to {@link com.tempvs.user.User}.
 */
@GrailsCompileStatic
class ItemGroup extends BasePersistent {

    String name
    String description

    static belongsTo = [user: User]

    static constraints = {
        description nullable: true
    }

    static mapping = {
    }

    List<Item> getItems() {
        Item.findAllByItemGroup(this)
    }
}
