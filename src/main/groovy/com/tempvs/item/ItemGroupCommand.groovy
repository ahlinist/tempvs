package com.tempvs.item

import com.tempvs.user.User
import grails.validation.Validateable

import static grails.util.Holders.applicationContext

/**
 * Command object used for creation of new {@link com.tempvs.item.ItemGroup} instance.
 */
class ItemGroupCommand implements Validateable {

    String name
    String description
    User user = applicationContext.userService.currentUser

    static constraints = {
        description nullable: true
    }
}
