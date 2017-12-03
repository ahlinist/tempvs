package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
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

    private static final String PERIOD_FIELD = 'period'

    UserService userService
    ImageService imageService
    ObjectDAOService objectDAOService

    ItemGroup getGroup(Object id) {
        objectDAOService.get(ItemGroup, id)
    }

    Item getItem(Object id) {
        objectDAOService.get(Item, id)
    }

    List<Source> getSourcesByItem(Item item) {
        List<Item2Source> items2Sources = Item2Source.findAllByItem(item)
        items2Sources*.source
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
    Item updateItem(Item item, List<Image> images = []) {
        images.each { Image image ->
            if (image) {
                item.addToImages(image)
            }
        }

        objectDAOService.save(item)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item editItemField(Item item, String fieldName, String fieldValue) {
        if (fieldName == PERIOD_FIELD) {
            throw new AccessDeniedException('Operation not supported.')
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
    Item deleteImage(Item item, Image image) {
        if (!item.images.contains(image)) {
            throw new AccessDeniedException('Item does not contain the given image.')
        }

        item.removeFromImages(image)
        imageService.deleteImage(image)
        objectDAOService.save(item)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item2Source linkSource(Item item, Source source) {
        objectDAOService.save([item: item, source: source] as Item2Source)
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void unlinkSource(Item item, Source source) {
        Item2Source item2Source = objectDAOService.find(Item2Source, [item: item, source: source])
        objectDAOService.delete(item2Source)
    }
}
