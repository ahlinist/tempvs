package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
 */
@Transactional
@GrailsCompileStatic
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

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#itemGroup.user.email == authentication.name')
    ItemGroup editItemGroupField(ItemGroup itemGroup, String fieldName, String fieldValue) {
        itemGroup."${fieldName}" = fieldValue
        itemGroup.save()
        itemGroup
    }

    @PreAuthorize('#itemGroup.user.email == authentication.name')
    void deleteGroup(ItemGroup itemGroup) {
        Collection<Item> items = itemGroup.items

        if (items) {
            List<Item2Passport> item2Passports = Item2Passport.findAllByItemInList(items)
            item2Passports*.delete()
            List<Item2Source> item2Sources = Item2Source.findAllByItemInList(items)
            item2Sources*.delete()
            Collection<Image> images = items*.images?.flatten() as Collection<Image>
            imageService.deleteImages(images)
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

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
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
        List<Item2Passport> item2Passports = Item2Passport.findAllByItem(item)
        item2Passports*.delete()
        List<Item2Source> item2Sources = Item2Source.findAllByItem(item)
        item2Sources*.delete()
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
    Item2Source linkSource(Item item, Source source) {
        Item2Source item2Source = [item: item, source: source] as Item2Source
        item2Source.save()
        item2Source
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name')
    void unlinkSource(Item item, Source source) {
        Item2Source.findByItemAndSource(item, source)?.delete()
    }

    Item addComment(Item item, Comment comment) {
        item.addToComments(comment)
        item.save()
        item
    }

    @PreAuthorize('#item.itemGroup.user.email == authentication.name or (#comment.userProfile != null and #comment.userProfile.user.email == authentication.name) or (#comment.clubProfile != null and #comment.clubProfile.user.email == authentication.name)')
    Item deleteComment(Item item, Comment comment) {
        item.removeFromComments(comment)
        item.save()
        item
    }
}
