package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.user.ClubProfile
import grails.compiler.GrailsCompileStatic

/**
 * An entity that represents {@link com.tempvs.user.ClubProfile}'s belongings.
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
