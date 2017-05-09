package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
/**
 * Service that manages {@link com.tempvs.item.Item} instances.
 */
@Transactional
@GrailsCompileStatic
class ItemService {

    SpringSecurityService springSecurityService
    ObjectFactory objectFactory
    ObjectDAO objectDAO

    ItemGroup createGroup(String name, String description) {
        Object currentUser = springSecurityService.currentUser

        if (currentUser) {
            User user = currentUser as User
            ItemGroup itemGroup = objectFactory.create(ItemGroup.class) as ItemGroup
            itemGroup.name = name
            itemGroup.description = description
            user.itemStash.addToItemGroups(itemGroup).save()
            itemGroup
        }
    }

    ItemStash getStash(String id) {
        Object object = objectDAO.get(ItemStash.class, id)

        if (object) {
            object as ItemStash
        }
    }

    ItemGroup getGroup(String id) {
        Object object = objectDAO.get(ItemGroup.class, id)

        if (object) {
            object as ItemGroup
        }
    }
}
