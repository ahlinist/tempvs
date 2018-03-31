package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.domain.BasePersistent
import club.tempvs.image.Image
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic

/**
 * An entity that corresponds a source for an {@link Item}
 */
@GrailsCompileStatic
class Source implements BasePersistent {

    String name
    String description
    ItemType itemType
    Period period
    List<Image> images
    List<Comment> comments

    static constraints = {
        name blank: false, size: 0..35
        description nullable: true, size: 0..2000
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
        comments cascade: 'all-delete-orphan'
    }
}
