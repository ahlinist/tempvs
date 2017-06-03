package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional

/**
 * Service that manages {@link com.tempvs.item.Item}, {@link com.tempvs.item.ItemGroup}
 * and {@link com.tempvs.item.ItemStash} instances.
 */
@Transactional
@GrailsCompileStatic
class ItemService {

    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'

    ImageService imageService
    UserService userService
    ObjectFactory objectFactory
    ObjectDAO objectDAO

    ItemGroup createGroup(String name, String description) {
        User user = userService.currentUser
        ItemGroup itemGroup = objectFactory.create(ItemGroup.class)
        itemGroup.name = name
        itemGroup.description = description
        itemGroup.itemStash = user.itemStash
        itemGroup.save()
        itemGroup
    }

    ItemStash getStash(String id) {
        objectDAO.get(ItemStash, id)
    }

    ItemGroup getGroup(String id) {
        objectDAO.get(ItemGroup, id)
    }

    Item getItem(String id) {
        objectDAO.get(Item, id)
    }

    Item createItem(String name, String description, Image itemImage, Image sourceImage, ItemGroup itemGroup) {
        Item item = objectFactory.create(Item.class)
        item.name = name
        item.description = description
        item.itemImageId = itemImage?.id
        item.sourceImageId = sourceImage?.id

        if (!itemGroup.attached) {
            itemGroup.attach()
        }

        item.itemGroup = itemGroup
        item.save()
        item
    }

    String deleteItem(String id) {
        Item item = getItem id

        if (item) {
            ItemGroup itemGroup = item.itemGroup

            if (itemGroup.itemStash.user == userService.currentUser) {
                String itemGroupId = itemGroup.id as String
                imageService.deleteImage(ITEM_IMAGE_COLLECTION, item.itemImageId)
                imageService.deleteImage(SOURCE_IMAGE_COLLECTION, item.sourceImageId)
                item.delete()
                itemGroupId
            } else {
                itemGroup.id as String
            }
        }
    }
}
