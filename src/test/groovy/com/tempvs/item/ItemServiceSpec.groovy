package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.image.Image
import com.tempvs.image.ImageService
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
    private static final String DESCRIPTION = 'description'

    def user = Mock(User)
    def item = Mock(Item)
    def image = Mock(Image)
    def objectDAO = Mock(ObjectDAO)
    def itemGroup = Mock(ItemGroup)
    def period = GroovyMock(Period)
    def userService = Mock(UserService)
    def imageService = Mock(ImageService)

    def setup() {
        service.objectDAO = objectDAO
        service.userService = userService
        service.imageService = imageService
    }

    def cleanup() {
    }

    void "Test createGroup()"() {
        when:
        def result = service.createGroup(itemGroup)

        then:
        1 * userService.currentUser >> user
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

    void "Test saveItem()"() {
        given:
        List<Image> images = [image, image]

        when:
        def result = service.saveItem(item, images)

        then:
        2 * item.addToImages(image)
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
        given:
        Set<Image> images = [image]

        when:
        def result = service.deleteItem(item)

        then:
        1 * item.images >> images
        1 * imageService.deleteImages(images)
        1 * item.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test failed deleteItem()"() {
        given:
        Set<Image> images = [image]

        when:
        def result = service.deleteItem(item)

        then:
        1 * item.images >> images
        1 * imageService.deleteImages(images)
        1 * item.delete([failOnError: true]) >> {throw new Exception()}
        0 * _

        result == Boolean.FALSE
    }

    void "Test deleteGroup()"() {
        given:
        Set<Item> items = [item]
        Set<Image> images = [image]

        when:
        def result = service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> items
        1 * item.getProperty(IMAGES) >> images
        1 * imageService.deleteImages(images)
        1 * itemGroup.delete([failOnError: true])
        0 * _

        result == Boolean.TRUE
    }

    void "Test failed deleteGroup()"() {
        given:
        Set<Item> items = [item]
        Set<Image> images = [image]

        when:
        def result = service.deleteGroup(itemGroup)

        then:
        1 * itemGroup.items >> items
        1 * item.getProperty(IMAGES) >> images
        1 * imageService.deleteImages(images)
        1 * itemGroup.delete([failOnError: true]) >> {throw new Exception()}
        0 * _

        result == Boolean.FALSE
    }

    void "Test editGroup()"() {
        given:
        Map properties = [name: NAME, description: DESCRIPTION]

        when:
        def result = service.editGroup(itemGroup, properties)

        then:
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * itemGroup.save() >> itemGroup
        0 * _

        and:
        result == itemGroup
    }
}
