package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import club.tempvs.user.User
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ItemServiceSpec extends Specification implements ServiceUnitTest<ItemService>, DataTest {

    private static final String NAME = 'name'
    private static final String IMAGES = 'images'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def item = Mock Item
    def image = Mock Image
    def source = Mock Source
    def comment = Mock Comment
    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup
    def item2Source = Mock Item2Source
    def item2Passport = Mock Item2Passport

    def imageService = Mock ImageService

    def setup() {
        GroovySpy(Item, global: true)
        GroovySpy(ItemGroup, global: true)
        GroovySpy(Item2Source, global: true)
        GroovySpy(Item2Passport, global: true)

        service.imageService = imageService
    }

    def cleanup() {
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
        1 * itemGroup.setName(FIELD_VALUE)
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test deleteGroup()"() {
        when:
        service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> [item]
        1 * Item2Passport.findAllByItemInList([item]) >> [item2Passport, item2Passport]
        2 * item2Passport.delete()
        1 * Item2Source.findAllByItemInList([item]) >> [item2Source, item2Source]
        2 * item2Source.delete()
        1 * item.getProperty(IMAGES) >> [image]
        1 * imageService.deleteImages([image])
        1 * itemGroup.delete()
        0 * _
    }

    void "Test updateItem()"() {
        when:
        def result = service.saveItem(item)

        then:
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test editItemField()"() {
        when:
        def result = service.editItemField(item, NAME, FIELD_VALUE)

        then:
        1 * item.setName(FIELD_VALUE)
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
        1 * Item2Source.findAllByItem(item) >> [item2Source, item2Source]
        2 * item2Source.delete()
        1 * item.images >> [image]
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
        1 * new Item2Source([item: item, source: source]) >> item2Source
        1 * item2Source.save() >> item2Source
        0 * _

        and:
        result == item2Source
    }

    void "Test unlinkSource()"() {
        when:
        service.unlinkSource(item, source)

        then:
        1 * Item2Source.findByItemAndSource(item, source) >> item2Source
        1 * item2Source.delete()
        0 * _
    }

    void "Test addComment()"() {
        when:
        def result = service.addComment(item, comment)

        then:
        1 * item.addToComments(comment)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }


    void "Test deleteComment()"() {
        when:
        def result = service.deleteComment(item, comment)

        then:
        1 * item.removeFromComments(comment)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }
}
