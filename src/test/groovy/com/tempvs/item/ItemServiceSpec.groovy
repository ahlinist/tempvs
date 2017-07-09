package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ItemService)
class ItemServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String ITEM_IMAGE = 'itemImage'
    private static final String SOURCE_IMAGE = 'sourceImage'
    private static final String COLLECTION = 'collection'

    def user = Mock(User)
    def item = Mock(Item)
    def item2 = Mock(Item)
    def image = Mock(Image)
    def objectDAO = Mock(ObjectDAO)
    def itemGroup = Mock(ItemGroup)
    def period = GroovyMock(Period)
    def userService = Mock(UserService)
    def imageService = Mock(ImageService)
    def objectFactory = Mock(ObjectFactory)
    def multipartFile = new MockMultipartFile(COLLECTION, "1234567" as byte[])


    def setup() {
        service.userService = userService
        service.objectFactory = objectFactory
        service.objectDAO = objectDAO
        service.imageService = imageService
    }

    def cleanup() {
    }

    void "Test createGroup() with fail"() {
        when:
        def result = service.createGroup(NAME, DESCRIPTION)

        then:
        1 * userService.currentUser >> user
        1 * objectFactory.create(ItemGroup.class) >> itemGroup
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * itemGroup.setUser(user)
        1 * itemGroup.save() >> null
        0 * _

        and:
        result == itemGroup
    }

    void "Test createGroup() with success"() {
        when:
        def result = service.createGroup(NAME, DESCRIPTION)

        then:
        1 * userService.currentUser >> user
        1 * objectFactory.create(ItemGroup.class) >> itemGroup
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * itemGroup.setUser(user)
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test getGroup()"() {
        when:
        def result = service.getGroup(ID)

        then:
        1 * objectDAO.get(ItemGroup, ID) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test createItem()"() {
        given:
        Map properties = [
                name: NAME,
                description:DESCRIPTION,
                period: period,
                itemGroup: itemGroup,
        ]

        when:
        def result = service.createItem(properties)

        then:
        1 * objectFactory.create(Item) >> item
        1 * item.setName(NAME)
        1 * item.setDescription(DESCRIPTION)
        1 * itemGroup.asType(ItemGroup) >> itemGroup
        1 * item.setItemGroup(itemGroup)
        1 * period.asType(Period) >> period
        1 * item.setPeriod(period)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test getItem()"() {
        when:
        def result = service.getItem(ID)

        then:
        1 * objectDAO.get(Item, ID) >> item
        0 * _

        and:
        result == item
    }

    void "Test deleteItem()"() {
        when:
        def result = service.deleteItem(item)

        then:
        1 * item.sourceImage >> null
        1 * item.itemImage >> image
        1 * imageService.deleteImages([image, null]) >> Boolean.TRUE
        1 * item.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test deleteGroup()"() {
        when:
        def result = service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> [item, item2]
        1 * item.getProperty(ITEM_IMAGE) >> image
        1 * item.getProperty(SOURCE_IMAGE) >> image
        1 * item2.getProperty(ITEM_IMAGE) >> image
        1 * item2.getProperty(SOURCE_IMAGE) >> null
        1 * imageService.deleteImages([image, image, image, null]) >> Boolean.TRUE
        1 * itemGroup.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test updateItem()"() {
        given: 'Props without images'
        Map properties = [
                name: NAME,
                description: DESCRIPTION,
                period: period,
                itemGroup: itemGroup,
        ]

        when:
        def result = service.updateItem(item, properties)

        then:
        1 * item.setName(NAME)
        1 * item.setDescription(DESCRIPTION)
        1 * period.asType(Period) >> period
        1 * item.setPeriod(period)
        1 * itemGroup.asType(ItemGroup) >> itemGroup
        1 * item.setItemGroup(itemGroup)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test updateItemImage()"() {
        when:
        def result = service.updateItemImage(item, multipartFile, IMAGE_INFO)

        then:
        1 * item.itemImage >> image
        1 * image.setImageInfo(IMAGE_INFO)
        1 * image.setCollection(ITEM_IMAGE_COLLECTION)
        1 * imageService.replaceImage(multipartFile, image) >> ID
        1 * image.setObjectId(ID)
        1 * item.setItemImage(image)
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }
}
