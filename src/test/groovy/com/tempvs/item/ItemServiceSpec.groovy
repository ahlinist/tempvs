package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.ImageService
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
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'
    private static final String ITEM_IMAGE_ID = 'itemImageId'
    private static final String SOURCE_IMAGE_ID = 'sourceImageId'

    def userService = Mock(UserService)
    def user = Mock(User)
    def itemGroup = Mock(ItemGroup)
    def item = Mock(Item)
    def item2 = Mock(Item)
    def objectFactory = Mock(ObjectFactory)
    def objectDAO = Mock(ObjectDAO)
    def imageService = Mock(ImageService)

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
        when:
        def result = service.createItem([:])

        then:
        1 * objectFactory.create(Item) >> item
        1 * item.save()
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
        1 * item.sourceImageId >> null
        1 * item.itemImageId >> ITEM_IMAGE_ID
        1 * imageService.deleteImageBeans(ITEM_IMAGE_COLLECTION, [ITEM_IMAGE_ID]) >> Boolean.TRUE
        1 * imageService.deleteImageBeans(SOURCE_IMAGE_COLLECTION, [null]) >> Boolean.TRUE
        1 * item.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test deleteGroup()"() {
        when:
        def result = service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> [item, item2]
        1 * item.getProperty(ITEM_IMAGE_ID) >> ITEM_IMAGE_ID
        1 * item.getProperty(SOURCE_IMAGE_ID) >> SOURCE_IMAGE_ID
        1 * item2.getProperty(ITEM_IMAGE_ID) >> ITEM_IMAGE_ID
        1 * item2.getProperty(SOURCE_IMAGE_ID) >> null
        1 * imageService.deleteImageBeans(ITEM_IMAGE_COLLECTION, [ITEM_IMAGE_ID, ITEM_IMAGE_ID]) >> Boolean.TRUE
        1 * imageService.deleteImageBeans(SOURCE_IMAGE_COLLECTION, [SOURCE_IMAGE_ID, null]) >> Boolean.TRUE
        1 * itemGroup.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test updateItem()"() {
        given: 'Props without images'
        Map properties = [:]

        when:
        def result = service.updateItem(item, properties)

        then:
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }
}
