package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@Transactional
@GrailsCompileStatic
class ItemService {

    private static final String ITEM_COLLECTION = 'item'

    ImageService imageService
    UserService userService
    ObjectDAO objectDAO

    ItemGroup getGroup(String id) {
        objectDAO.get(ItemGroup, id)
    }

    Item getItem(String id) {
        objectDAO.get(Item, id)
    }

    ItemGroup createGroup(ItemGroup itemGroup) {
        itemGroup.user = userService.currentUser
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
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

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item createItem(Item item, Map properties) {
        Set<Image> itemImages = imageService.updateImages(properties.imageBeans as List<ImageUploadBean>, ITEM_COLLECTION)

        if (itemImages) {
            item.images = itemImages
        }

        item.save()
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Boolean deleteItem(Item item) {
        imageService.deleteImages(item.images)

        try {
            item.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item updateItem(Item item, Map properties) {
        InvokerHelper.setProperties(item, properties)
        item.save()
        item
    }
}
