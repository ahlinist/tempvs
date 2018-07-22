package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.domain.BasePersistent
import club.tempvs.image.Image
import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import grails.compiler.GrailsCompileStatic

/**
 * An entity that represents {@link Profile}'s belongings.
 */
@GrailsCompileStatic
class Passport implements BasePersistent {

    String name
    String description
    List<Image> images
    List<Comment> comments

    static belongsTo = [profile: Profile]

    static constraints = {
        name blank: false, size: 0..35
        description nullable: true, size: 0..2000
        profile validator: { Profile profile ->
            profile.type == ProfileType.CLUB
        }
    }

    static mapping = {
        images cascade: 'all-delete-orphan'
        comments cascade: 'all-delete-orphan'
    }
}
