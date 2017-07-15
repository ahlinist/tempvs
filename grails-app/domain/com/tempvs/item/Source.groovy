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

    static hasMany = [images: Image]

    static constraints = {
        description nullable: true
        images nullable: true
    }
}
