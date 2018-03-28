package club.tempvs.item

import club.tempvs.domain.BasePersistent
import club.tempvs.user.User
import grails.compiler.GrailsCompileStatic

/**
 * Group of {@link Item} that belong to {@link User}.
 */
@GrailsCompileStatic
class ItemGroup implements BasePersistent {

    String name
    String description
    List<Item> items

    static hasMany = [items: Item]
    static belongsTo = [user: User]

    static constraints = {
        name blank: false, size: 0..35
        description nullable: true, size: 0..2000
    }
}
