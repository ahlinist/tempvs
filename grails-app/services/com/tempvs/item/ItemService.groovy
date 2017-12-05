package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.UserService
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@Transactional
class ItemService {

    private static final String PERIOD_FIELD = 'period'

    UserService userService
    ImageService imageService

    ItemGroup getGroup(Object id) {
        ItemGroup.get id
    }

    Item getItem(Object id) {
        Item.get id
    }

    List<Source> getSourcesByItem(Item item) {
        List<Item2Source> items2Sources = Item2Source.findAllByItem(item)
        items2Sources*.source
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup createGroup(ItemGroup itemGroup) {
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup editItemGroupField(ItemGroup itemGroup, String fieldName, String fieldValue) {
        itemGroup."${fieldName}" = fieldValue
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    void deleteGroup(ItemGroup itemGroup) {
        List<Item> items = itemGroup.items as List
        imageService.deleteImages(items*.images?.flatten())
        List<Item2Source> item2Sources = Item2Source.findAllByItemInList(items)
        item2Sources*.delete()
        itemGroup.delete()
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item updateItem(Item item, List<Image> images = []) {
        images.each { Image image ->
            if (image) {
                item.addToImages(image)
            }
        }

        item.save()
        item
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item editItemField(Item item, String fieldName, String fieldValue) {
        if (fieldName == PERIOD_FIELD) {
            throw new AccessDeniedException('Operation not supported.')
        } else {
            item."${fieldName}" = fieldValue
        }

        item.save()
        item
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void deleteItem(Item item) {
        imageService.deleteImages(item.images)
        Item2Source.findAllByItem(item)*.delete()
        item.delete()
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item deleteImage(Item item, Image image) {
        if (!item.images.contains(image)) {
            throw new AccessDeniedException('Item does not contain the given image.')
        }

        item.removeFromImages(image)
        imageService.deleteImage(image)
        item.save()
        item
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item2Source linkSource(Item item, Source source) {
        Item2Source item2Source = [item: item, source: source] as Item2Source
        item2Source.save()
        item2Source
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void unlinkSource(Item item, Source source) {
        Item2Source item2Source = Item2Source.findByItemAndSource(item, source)
        item2Source.delete()
    }
}
