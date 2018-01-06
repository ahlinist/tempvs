package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import com.tempvs.user.User
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ItemServiceSpec extends Specification implements ServiceUnitTest<ItemService>, DomainUnitTest<Item> {

    private static final Long LONG_ONE = 1L
    private static final String NAME = 'name'
    private static final String ITEMS = 'items'
    private static final String IMAGES = 'images'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def item = Mock Item
    def image = Mock Image
    def source = Mock Source
    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup
    def item2Passport = Mock Item2Passport

    def imageService = Mock ImageService

    def setup() {
        GroovySpy(Item, global: true)
        GroovySpy(ItemGroup, global: true)
        GroovySpy(Item2Passport, global: true)

        service.imageService = imageService
    }

    def cleanup() {
    }

    void "Test getGroup()"() {
        when:
        def result = service.getGroup(LONG_ONE)

        then:
        1 * ItemGroup.get(LONG_ONE) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test getItem()"() {
        when:
        def result = service.getItem(LONG_ONE)

        then:
        1 * Item.get(LONG_ONE) >> item
        0 * _

        and:
        result == item
    }

    void "Test getItemsByPeriod()"() {
        when:
        def result = service.getItemsByPeriod(period)

        then:
        1 * Item.findAllByPeriod(period) >> [item]
        0 * _

        and:
        result == [item]
    }

    void "Test createGroup()"() {
        when:
        def result = service.createGroup(itemGroup)

        then:
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test editItemGroupField()"() {
        when:
        def result = service.editItemGroupField(itemGroup, NAME, FIELD_VALUE)

        then:
        1 * itemGroup.setProperty(NAME, FIELD_VALUE)
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test deleteGroup()"() {
        when:
        service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.getProperty(ITEMS) >> [item]
        1 * Item2Passport.findAllByItemInList([item]) >> [item2Passport, item2Passport]
        2 * item2Passport.delete()
        1 * item.getProperty(IMAGES) >> [image]
        1 * imageService.deleteImages([image])
        1 * itemGroup.delete()
        0 * _
    }

    void "Test updateItem()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.updateItem(item, images)

        then:
        2 * item.addToImages(image)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test editItemField()"() {
        when:
        def result = service.editItemField(item, NAME, FIELD_VALUE)

        then:
        1 * item.setProperty(NAME, FIELD_VALUE)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test deleteItem()"() {
        when:
        service.deleteItem(item)

        then:
        1 * Item2Passport.findAllByItem(item) >> [item2Passport, item2Passport]
        2 * item2Passport.delete()
        1 * item.getProperty(IMAGES) >> [image]
        1 * imageService.deleteImages([image])
        1 * item.delete()
        0 * _
    }

    void "Test deleteImage()"() {
        given:
        List<Image> images = [image]

        when:
        def result = service.deleteImage(item, image)

        then:
        1 * item.getProperty(IMAGES) >> images
        1 * item.removeFromImages(image)
        1 * imageService.deleteImage(image)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test linkSource()"() {
        when:
        def result = service.linkSource(item, source)

        then:
        1 * item.addToSources(source)
        1 * item.save()
        0 * _

        and:
        result == item
    }

    void "Test unlinkSource()"() {
        when:
        def result = service.unlinkSource(item, source)

        then:
        1 * item.removeFromSources(source)
        1 * item.save()
        0 * _

        and:
        result == item
    }
}
