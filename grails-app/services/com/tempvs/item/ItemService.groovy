package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@Transactional
@GrailsCompileStatic
class ItemService {

    private static final String ITEM_COLLECTION = 'item'

    ImageService imageService
    UserService userService
    ObjectFactory objectFactory
    ObjectDAO objectDAO

    ItemGroup createGroup(Map properties) {
        ItemGroup itemGroup = objectFactory.create(ItemGroup.class)
        InvokerHelper.setProperties(itemGroup, properties)
        itemGroup.user = userService.currentUser
        itemGroup.save()
        itemGroup
    }

    ItemGroup getGroup(String id) {
        objectDAO.get(ItemGroup, id)
    }

    Item getItem(String id) {
        objectDAO.get(Item, id)
    }

    Item createItem(Map properties) {
        Item item = objectFactory.create(Item)
        Set<Image> itemImages = imageService.extractImages(properties.imageBeans as List<ImageUploadBean>, ITEM_COLLECTION)

        if (itemImages) {
            item.images = itemImages
        }

        updateItem(item, properties)
    }

    Boolean deleteItem(Item item) {
        imageService.deleteImages(item.images)

        try {
            item.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    Boolean deleteGroup(ItemGroup itemGroup) {
        Set<Item> items = itemGroup.items
        imageService.deleteImages(items*.images?.flatten() as Set<Image>)

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
