package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode
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
        if (id) {
            objectDAOService.get(Item, id)
        }
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup createGroup(ItemGroup itemGroup) {
        objectDAOService.save(itemGroup)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup editItemGroupField(ItemGroup itemGroup, String fieldName, String fieldValue) {
        itemGroup."${fieldName}" = fieldValue
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

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item editItemField(Item item, String fieldName, String fieldValue) {
        if (fieldName == 'source') {
            item.source = sourceService.getSource(fieldValue)
        } else if (fieldName == 'period') {
            try {
                item.period = Period.valueOf(fieldValue)
                item.sources.clear()
            } catch (IllegalArgumentException exception) {
                item.period = null
                item.sources.clear()
            }
        } else {
            item."${fieldName}" = fieldValue
        }

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

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item linkSource(Item item, Source source) {
        item.addToSources(source)
        objectDAOService.save(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item unlinkSource(Item item, Source source) {
        item.removeFromSources(source)
        objectDAOService.save(item)
    }
}
