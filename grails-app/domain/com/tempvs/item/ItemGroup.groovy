package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic

/**
 * Group of {@link com.tempvs.item.Item} that belong to {@link com.tempvs.user.User}.
 */
@GrailsCompileStatic
class ItemGroup implements BasePersistent {

    String name
    String description
    Collection<Item> items

    static hasMany = [items: Item]
    static belongsTo = [user: User]

    static constraints = {
        name blank: false, size: 0..35
        description nullable: true, size: 0..2000
    }
}
