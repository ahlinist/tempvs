package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.gorm.transactions.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@Transactional
class ItemService {

    private static final String TYPE_FIELD = 'type'
    private static final String PERIOD_FIELD = 'period'
    private static final String ITEM_GROUP = 'itemGroup'

    ImageService imageService

    ItemGroup getGroup(Long id) {
        ItemGroup.get id
    }

    Item getItem(Long id) {
        Item.get id
    }

    List<Item> getItemsByPeriod(Period period) {
        Item.findAllByPeriod(period)
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
        List<Item> items = itemGroup.items

        if (items) {
            imageService.deleteImages(items*.images?.flatten())
        }

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
        if (fieldName in [PERIOD_FIELD, ITEM_GROUP, TYPE_FIELD]) {
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
    Item linkSource(Item item, Source source) {
        item.addToSources(source)
        item.save()
        item
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    Item unlinkSource(Item item, Source source) {
        item.removeFromSources(source)
        item.save()
        item
    }
}
