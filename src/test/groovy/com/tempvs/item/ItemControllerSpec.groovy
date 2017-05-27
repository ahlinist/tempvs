package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.User
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ItemController)
class ItemControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_STASH = 'itemStash'
    private static final String ITEM_IMAGE = 'itemImage'
    private static final String SOURCE_IMAGE = 'sourceImage'
    private static final String ITEM_GROUP = 'itemGroup'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String ITEM_URI = '/item/show'

    def user = Mock(User)
    def json = Mock(JSON)
    def item = Mock(Item)
    def image = Mock(Image)
    def itemGroup = Mock(ItemGroup)
    def itemStash = Mock(ItemStash)
    def itemImage = Mock(Image)
    def sourceImage = Mock(Image)
    def springSecurityService = Mock(SpringSecurityService)
    def itemService = Mock(ItemService)
    def imageService = Mock(ImageService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def createItemCommand = Mock(CreateItemCommand)
    def createItemGroupCommand = Mock(CreateItemGroupCommand)

    def setup() {
        controller.springSecurityService = springSecurityService
        controller.itemService = itemService
        controller.ajaxResponseService = ajaxResponseService
        controller.imageService = imageService
    }

    def cleanup() {
    }

    void "Test stash() without id"() {
        when:
        def result = controller.stash()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(ITEM_STASH) >> itemStash
        0 * _

        and:
        result == [itemStash: itemStash]
    }

    void "Test stash() with id for existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> itemStash
        0 * _

        and:
        result == [itemStash: itemStash]
    }

    void "Test stash() with id for non-existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> null
        0 * _

        and:
        result == [itemStash: null]
    }

    void "Test createGroup() rendering"() {
        when:
        controller.createGroup()

        then:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group creation failing"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when: 'ItemGroup has not been created'
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(createItemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(NAME) >> NAME
        1 * createItemGroupCommand.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(itemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(NAME) >> NAME
        1 * createItemGroupCommand.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.TRUE
        1 * itemGroup.getProperty(ID) >> ONE
        0 * _

        and:
        response.json.redirect == "${ITEM_GROUP_URI}/${ONE}"
    }

    void "Test group() without id"() {
        when:
        controller.group()

        then:
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group() for non-existing id"() {
        when:
        params.id = ONE
        controller.group()

        then:
        1 * itemService.getGroup(ONE) >> null
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group() for existing id"() {
        when:
        params.id = ONE
        def result = controller.group()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        0 * _

        and:
        result == [itemGroup: itemGroup]
    }

    void "Test createItem() rendering"() {
        when:
        controller.createItem()

        then:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test createItem()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE
        flash.itemGroup = itemGroup
        def multipartItemImage = new MockMultipartFile(ITEM_IMAGE, "1234567" as byte[])
        def multipartSourceImage = new MockMultipartFile(SOURCE_IMAGE, "1234567" as byte[])
        controller.request.addFile(multipartItemImage)
        controller.request.addFile(multipartSourceImage)
        controller.session.itemGroup = itemGroup

        when:
        controller.createItem(createItemCommand)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(ID) >> ID
        1 * createItemCommand.validate() >> Boolean.TRUE
        1 * createItemCommand.getProperty(NAME) >> NAME
        1 * createItemCommand.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * createItemCommand.getProperty(ITEM_IMAGE) >> multipartItemImage
        1 * createItemCommand.getProperty(SOURCE_IMAGE) >> multipartSourceImage
        1 * imageService.createImage(_ as InputStream, _ as String, _ as Map) >> itemImage
        1 * imageService.createImage(_ as InputStream, _ as String, _ as Map) >> sourceImage
        1 * itemService.createItem(NAME, DESCRIPTION, itemImage, sourceImage, itemGroup) >> item
        1 * itemGroup.getProperty(ID) >> ID
        1 * item.validate() >> Boolean.TRUE
        1 * item.getProperty(ID) >> ONE
        0 * _

        and:
        response.json.redirect == "${ITEM_URI}/${ONE}"
    }

    void "Test show() without id"() {
        when:
        controller.show()

        then:
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl

    }

    void "Test show() with id"() {
        given:
        params.id = ONE

        when:
        def result = controller.show()

        then:
        1 * itemService.getItem(ONE) >> item
        0 * _

        and:
        result == [item: item]
    }
}
