package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ItemService)
class ItemServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String NAME = 'name'
    private static final String IMAGES = 'images'
    private static final String ITEM_COLLECTION = 'item'
    private static final String COLLECTION = 'collection'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def item = Mock Item
    def image = Mock Image
    def period = Period.XIX
    def source = Mock Source
    def itemGroup = Mock ItemGroup
    def userService = Mock UserService
    def imageService = Mock ImageService
    def imageUploadBean = Mock ImageUploadBean
    def objectDAOService = Mock ObjectDAOService

    def setup() {
        service.userService = userService
        service.imageService = imageService
        service.objectDAOService = objectDAOService
    }

    def cleanup() {
    }

    void "Test getGroup()"() {
        when:
        def result = service.getGroup(ID)

        then:
        1 * objectDAOService.get(ItemGroup, ID) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test getItem()"() {
        when:
        def result = service.getItem(ID)

        then:
        1 * objectDAOService.get(Item, ID) >> item
        0 * _

        and:
        result == item
    }

    void "Test createGroup()"() {
        when:
        def result = service.createGroup(itemGroup)

        then:
        1 * objectDAOService.save(itemGroup) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test editItemGroupField()"() {
        when:
        def result = service.editItemGroupField(itemGroup, NAME, FIELD_VALUE)

        then:
        1 * itemGroup.setName(FIELD_VALUE)
        1 * objectDAOService.save(itemGroup) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test deleteGroup()"() {
        given:
        Set<Item> items = [item]
        Set<Image> images = [image]

        when:
        service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> items
        1 * item.getProperty(IMAGES) >> images
        1 * imageService.deleteImages(images)
        1 * objectDAOService.delete(itemGroup)
        0 * _
    }

    void "Test updateItem()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.updateItem(item, images)

        then:
        2 * item.addToImages(image)
        1 * objectDAOService.save(item) >> item
        0 * _

        and:
        result == item
    }

    void "Test editItemField()"() {
        when:
        def result = service.editItemField(item, NAME, FIELD_VALUE)

        then:
        1 * item.setName(FIELD_VALUE)
        1 * objectDAOService.save(item) >> item
        0 * _

        and:
        result == item
    }

    void "Test deleteItem()"() {
        given:
        Set<Image> images = [image]

        when:
        service.deleteItem(item)

        then:
        1 * item.images >> images
        1 * imageService.deleteImages(images)
        1 * objectDAOService.delete(item)
        0 * _
    }

    void "Test deleteImage()"() {
        given:
        List<Image> images = [image]

        when:
        def result = service.deleteImage(item, image)

        then:
        1 * item.images >> images
        1 * item.removeFromImages(image)
        1 * imageService.deleteImage(image)
        1 * objectDAOService.save(item) >> item
        0 * _

        and:
        result == item
    }

    void "Test linkSource()"() {
        when:
        def result = service.linkSource(item, source)

        then:
        1 * item.addToSources(source)
        1 * objectDAOService.save(item) >> item
        0 * _

        and:
        result == item
    }

    void "Test unlinkSource()"() {
        when:
        def result = service.unlinkSource(item, source)

        then:
        1 * item.removeFromSources(source)
        1 * objectDAOService.save(item) >> item
        0 * _

        and:
        result == item
    }
}
