package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
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
    private static final String IMAGE_ID = 'imageId'
    private static final String DESCRIPTION = 'description'
    private static final Long LONG_ID = 1L
    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'

    def userService = Mock(UserService)
    def user = Mock(User)
    def itemStash = Mock(ItemStash)
    def itemGroup = Mock(ItemGroup)
    def item = Mock(Item)
    def itemImage = Mock(Image)
    def sourceImage = Mock(Image)
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
        1 * user.getItemStash() >> itemStash
        1 * itemGroup.setItemStash(itemStash)
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
        1 * user.getItemStash() >> itemStash
        1 * itemGroup.setItemStash(itemStash)
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }

    void "Test getStash()"() {
        when:
        def result = service.getStash(ID)

        then:
        1 * objectDAO.get(ItemStash, ID) >> itemStash
        0 * _

        and:
        result == itemStash
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
        String ITEM_IMAGE_ID = 'itemImageId'
        String SOURCE_IMAGE_ID = 'sourceImageId'

        when:
        def result = service.createItem(NAME, DESCRIPTION, itemImage, sourceImage, itemGroup)

        then:
        1 * itemImage.getId() >> ITEM_IMAGE_ID
        1 * sourceImage.getId() >> SOURCE_IMAGE_ID
        1 * objectFactory.create(Item.class) >> item
        1 * item.setName(NAME)
        1 * item.setDescription(DESCRIPTION)
        1 * item.setItemImageId(ITEM_IMAGE_ID)
        1 * item.setSourceImageId(SOURCE_IMAGE_ID)
        1 * item.setItemGroup(itemGroup)
        1 * itemGroup.isAttached() >> Boolean.FALSE
        1 * itemGroup.attach()
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
        def result = service.deleteItem(ID)

        then:
        1 * objectDAO.get(Item, ID) >> item
        1 * userService.currentUser >> user
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.itemStash >> itemStash
        1 * itemStash.user >> user
        1 * itemGroup.id >> LONG_ID
        1 * item.sourceImageId >> null
        1 * item.itemImageId >> IMAGE_ID
        1 * imageService.deleteImage(ITEM_IMAGE_COLLECTION, IMAGE_ID) >> Boolean.TRUE
        1 * imageService.deleteImage(SOURCE_IMAGE_COLLECTION, null) >> Boolean.TRUE
        1 * item.delete()
        0 * _

        result == LONG_ID as String
    }
}
