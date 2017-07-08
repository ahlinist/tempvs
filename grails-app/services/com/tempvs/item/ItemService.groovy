package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.web.multipart.MultipartFile

/**
 * Service that manages {@link com.tempvs.item.Item} and {@link com.tempvs.item.ItemGroup} instances.
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
        ItemGroup itemGroup = objectFactory.create(ItemGroup.class)
        itemGroup.name = name
        itemGroup.description = description
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
        InvokerHelper.setProperties(item, properties)
        item.save()
        item
    }

    Boolean deleteItem(Item item) {
        imageService.deleteImages([item.itemImage, item.sourceImage])

        try {
            item.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    Boolean deleteGroup(ItemGroup itemGroup) {
        Set<Item> items = itemGroup.items
        imageService.deleteImages(items*.itemImage + items*.sourceImage)

        try {
            itemGroup.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    Item updateItemImage(Item item, MultipartFile multipartFile, String imageInfo) {
        Image image = item.itemImage ?: objectFactory.create(Image) as Image
        image.imageInfo = imageInfo
        image.collection = ITEM_IMAGE_COLLECTION
        image.objectId = imageService.replaceImage(multipartFile, image)
        item.itemImage = image
        item.save()
        item
    }

    Item updateSourceImage(Item item, MultipartFile multipartFile, String imageInfo) {
        Image image = item.sourceImage ?: objectFactory.create(Image) as Image
        image.imageInfo = imageInfo
        image.collection = SOURCE_IMAGE_COLLECTION
        image.objectId = imageService.replaceImage(multipartFile, image)
        item.sourceImage = image
        item.save()
        item
    }

    Item updateItem(Item item, Map properties) {
        InvokerHelper.setProperties(item, properties)
        item.save()
        item
    }
}
