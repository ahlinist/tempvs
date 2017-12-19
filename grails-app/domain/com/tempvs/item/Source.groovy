package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds a source for an {@link com.tempvs.item.Item}
 */
@GrailsCompileStatic
class Source extends BasePersistent {

    String name
    String description
    Period period
    List<Image> images

    static constraints = {
        description nullable: true
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
    }
}
