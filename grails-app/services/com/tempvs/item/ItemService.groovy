package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.image.Image
import com.tempvs.image.ImageService
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

    ImageService imageService
    UserService userService
    ObjectDAO objectDAO

    ItemGroup getGroup(Object id) {
        objectDAO.get(ItemGroup, id)
    }

    Item getItem(Object id) {
        objectDAO.get(Item, id)
    }

    ItemGroup createGroup(ItemGroup itemGroup) {
        itemGroup.user = userService.currentUser
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup editGroup(ItemGroup itemGroup, Map properties) {
        InvokerHelper.setProperties(itemGroup, properties)
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item saveItem(Item item, List<Image> images = null) {
        images?.each { Image image ->
            item.addToImages(image)
        }

        item.save()
        item
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    Boolean deleteGroup(ItemGroup itemGroup) {
        try {
            imageService.deleteImages(itemGroup.items*.images?.flatten() as Set<Image>)
            itemGroup.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Boolean deleteItem(Item item) {
        try {
            imageService.deleteImages(item.images)
            item.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Boolean deleteItemImage(Item item, Image image) {
        try {
            item.removeFromImages(image)
            imageService.deleteImage(image)
            item.save(failOnError: true)
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
