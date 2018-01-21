package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.domain.BasePersistent
import com.tempvs.user.ClubProfile
import grails.compiler.GrailsCompileStatic

/**
 * An entity that represents {@link com.tempvs.user.ClubProfile}'s belongings.
 */
@GrailsCompileStatic
class Passport implements BasePersistent {

    String name
    String description
    List<Comment> comments

    static belongsTo = [clubProfile: ClubProfile]

    static constraints = {
        description nullable: true, blank: true
    }

    static mapping = {
        comments cascade: 'all-delete-orphan'
    }

    List<Item> getItems() {
        List<Item2Passport> item2Passports = Item2Passport.findAllByPassport(this)
        item2Passports*.item
    }
}
