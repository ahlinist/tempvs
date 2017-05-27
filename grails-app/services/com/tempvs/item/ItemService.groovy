package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
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

    Item getItem(String id) {
        Object object = objectDAO.get(Item.class, id)

        if (object) {
            object as Item
        }
    }

    Item createItem(String name, String description, Image itemImage, Image sourceImage, ItemGroup itemGroup) {
        Item item = objectFactory.create(Item.class) as Item
        item.setName(name)
        item.setDescription(description)
        item.setItemImageId(itemImage?.id)
        item.setSourceImageId(sourceImage?.id)

        if (!itemGroup.attached) {
            itemGroup.attach()
        }

        itemGroup.addToItems(item).save()
        item
    }
}
