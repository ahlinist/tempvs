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
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def item = Mock Item
    def image = Mock Image
    def source = Mock Source
    def period = GroovyMock Period
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
}
