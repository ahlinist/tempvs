package com.tempvs.item

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

    static belongsTo = [clubProfile: ClubProfile]

    static constraints = {
        description nullable: true, blank: true
    }

    List<Item> getItems() {
        List<Item2Passport> item2Passports = Item2Passport.findAllByPassport(this)
        item2Passports*.item
    }
}
