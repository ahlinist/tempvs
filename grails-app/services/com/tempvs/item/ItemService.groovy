package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.ImageService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

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

    Item createItem(Map properties) {
        Item item = objectFactory.create(Item.class)
        InvokerHelper.setProperties(item, properties)
        item.save()
        item
    }

    Boolean deleteItem(Item item) {
        imageService.deleteImages(ITEM_IMAGE_COLLECTION, [item.itemImageId])
        imageService.deleteImages(SOURCE_IMAGE_COLLECTION, [item.sourceImageId])

        try {
            item.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    Boolean deleteGroup(ItemGroup itemGroup) {
        Set<Item> items = itemGroup.items
        imageService.deleteImages(ITEM_IMAGE_COLLECTION, items*.itemImageId)
        imageService.deleteImages(SOURCE_IMAGE_COLLECTION, items*.sourceImageId)

        try {
            itemGroup.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    Item updateItem(Item item, Map properties) {
        InvokerHelper.setProperties(item, properties)
        item.save()
        item
    }
}
