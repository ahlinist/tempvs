package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@GrailsCompileStatic
class ItemService {

    private static final String ITEM_COLLECTION = 'item'

    UserService userService
    ImageService imageService
    SourceService sourceService
    ObjectDAOService objectDAOService

    ItemGroup getGroup(Object id) {
        objectDAOService.get(ItemGroup, id)
    }

    Item getItem(Object id) {
        objectDAOService.get(Item, id)
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup createGroup(ItemGroup itemGroup) {
        objectDAOService.save(itemGroup)
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup editItemGroupField(ItemGroup itemGroup, String fieldName, String fieldValue) {
        InvokerHelper.setProperties(itemGroup, ["${fieldName}": fieldValue])
        objectDAOService.save(itemGroup)
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    void deleteGroup(ItemGroup itemGroup) {
        imageService.deleteImages(itemGroup.items*.images?.flatten() as Set<Image>)
        objectDAOService.delete(itemGroup)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item updateItem(Item item, List<ImageUploadBean> imageUploadBeans) {
        List<Image> images = imageService.uploadImages(imageUploadBeans, ITEM_COLLECTION)

        images?.each { Image image ->
            item.addToImages(image)
        }

        objectDAOService.save(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item editItemImage(Item item, Image image, ImageUploadBean imageUploadBean) {
        if (!item.images.contains(image)) {
            throw new AccessDeniedException('Item does not contain the given image.')
        }

        imageService.updateImage(imageUploadBean, image.collection, image)
        objectDAOService.save(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item editItemField(Item item, String fieldName, String fieldValue) {
        Map properties

        if (fieldName == 'source') {
            properties = [source: sourceService.getSource(fieldValue)]
        } else if (fieldName == 'period') {
            try {
                properties = [period: Period.valueOf(fieldValue), source: null]
            } catch (IllegalArgumentException exception) {
                item.period = null
            }
        } else {
            properties = ["${fieldName}": fieldValue]
        }

        InvokerHelper.setProperties(item, properties)
        objectDAOService.save(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void deleteItem(Item item) {
        imageService.deleteImages(item.images)
        objectDAOService.delete(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void deleteItemImage(Item item, Image image) {
        if (!item.images.contains(image)) {
            throw new AccessDeniedException('Item does not contain the given image.')
        }

        item.removeFromImages(image)
        imageService.deleteImage(image)
        objectDAOService.save(item)
    }
}
