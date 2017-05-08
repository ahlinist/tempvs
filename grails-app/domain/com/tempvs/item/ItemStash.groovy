package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic

/**
 * Stash of {@link com.tempvs.item.Item} that belong to {@link com.tempvs.user.User}.
 */
@GrailsCompileStatic
class ItemStash extends BasePersistent {

    static belongsTo = [user: User]
    static hasMany = [itemGroups: ItemGroup]

    static constraints = {
    }
}
