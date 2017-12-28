package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.user.ClubProfile

/**
 * An entity that represents {@link com.tempvs.user.ClubProfile}'s belognings.
 */
class Passport extends BasePersistent {

    String name
    String description
    ClubProfile clubProfile

    List<Item> getItems() {
        Item2Passport.findAllByPassport(this)?.item
    }

    static constraints = {
        description nullable: true, blank: true
    }
}
