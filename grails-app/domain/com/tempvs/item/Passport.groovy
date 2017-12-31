package com.tempvs.item

import com.tempvs.domain.BasePersistent
import com.tempvs.user.ClubProfile

/**
 * An entity that represents {@link com.tempvs.user.ClubProfile}'s belongings.
 */
class Passport extends BasePersistent {

    String name
    String description

    static belongsTo = [clubProfile: ClubProfile]
    static hasMany = [items: Item]

    static constraints = {
        description nullable: true, blank: true
    }
}
