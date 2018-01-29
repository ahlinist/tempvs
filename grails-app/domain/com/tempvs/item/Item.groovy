package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds the reconstructed item.
 */
@GrailsCompileStatic
class Item implements BasePersistent {

    String name
    String description
    Type type
    Period period
    Collection<Image> images
    List<Comment> comments

    List<Source> getSources() {
        List<Item2Source> item2Sources = Item2Source.findAllByItem(this)
        item2Sources*.source
    }

    static belongsTo = [itemGroup: ItemGroup]

    static constraints = {
        name blank: false
        description nullable: true
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
        comments cascade: 'all-delete-orphan'
    }
}
