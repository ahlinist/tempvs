package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds a source for an {@link com.tempvs.item.Item}
 */
@GrailsCompileStatic
class Source implements BasePersistent {

    String name
    String description
    Type type
    Period period
    Collection<Image> images
    List<Comment> comments

    static constraints = {
        description nullable: true
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
        comments cascade: 'all-delete-orphan'
    }
}
