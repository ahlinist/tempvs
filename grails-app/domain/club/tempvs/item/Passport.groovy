package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.domain.BasePersistent
import club.tempvs.image.Image
import club.tempvs.user.ClubProfile
import grails.compiler.GrailsCompileStatic

/**
 * An entity that represents {@link ClubProfile}'s belongings.
 */
@GrailsCompileStatic
class Passport implements BasePersistent {

    String name
    String description
    List<Image> images
    List<Comment> comments

    static belongsTo = [clubProfile: ClubProfile]

    static constraints = {
        name blank: false, size: 0..35
        description nullable: true, size: 0..2000
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
        comments cascade: 'all-delete-orphan'
    }
}
