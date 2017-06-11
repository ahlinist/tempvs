package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import com.tempvs.user.UserService
import grails.converters.JSON
import grails.test.mixin.TestFor
import grails.web.mapping.LinkGenerator
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ItemController)
class ItemControllerSpec extends Specification {

    private static final Long LONG_ID = 1L
    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE = 'itemImage'
    private static final String SOURCE_IMAGE = 'sourceImage'
    private static final String ITEM_IMAGE_ID = 'itemImageId'
    private static final String SOURCE_IMAGE_ID = 'sourceImageId'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String ITEM_STASH_URI = '/item/stash'
    private static final String ITEM_URI = '/item/show'
    private static final String GROUP_ACTION = 'group'
    private static final String SHOW_ACTION = 'show'
    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'

    def user = Mock(User)
    def json = Mock(JSON)
    def item = Mock(Item)
    def updatedItem = Mock(Item)
    def image = Mock(Image)
    def itemGroup = Mock(ItemGroup)
    def itemStash = Mock(ItemStash)
    def itemImage = Mock(Image)
    def sourceImage = Mock(Image)
    def userService = Mock(UserService)
    def userProfile = Mock(UserProfile)
    def itemService = Mock(ItemService)
    def imageService = Mock(ImageService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def itemCommand = Mock(ItemCommand)
    def createItemGroupCommand = Mock(CreateItemGroupCommand)
    def grailsLinkGenerator = Mock(LinkGenerator)
    def multipartItemImage = new MockMultipartFile(ITEM_IMAGE, "1234567" as byte[])
    def multipartSourceImage = new MockMultipartFile(SOURCE_IMAGE, "1234567" as byte[])

    def setup() {
        controller.userService = userService
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
        1 * userService.currentUser >> user
        1 * user.itemStash >> itemStash
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        0 * _

        and:
        result == [itemStash: itemStash, userProfile: userProfile, ownStash: Boolean.TRUE]
    }

    void "Test stash() with id for existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> itemStash
        1 * itemStash.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        0 * _

        and:
        result == [itemStash: itemStash, userProfile: userProfile, ownStash: Boolean.TRUE]
    }

    void "Test stash() with id for non-existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> null
        0 * _

        and:
        !controller.modelAndView
        !response.redirectedUrl
        !result
    }

    void "Test group creation against invalid command"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(createItemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation against invalid group"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getName() >> NAME
        1 * createItemGroupCommand.getDescription() >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(itemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation"() {
        given:
        Map linkGeneratorMap = ['action':GROUP_ACTION, 'id':LONG_ID]
        controller.grailsLinkGenerator = grailsLinkGenerator

        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getName() >> NAME
        1 * createItemGroupCommand.getDescription() >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.TRUE
        1 * itemGroup.getId() >> LONG_ID
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_GROUP_URI}/${LONG_ID}"
        0 * _

        and:
        response.json.redirect == "${ITEM_GROUP_URI}/${LONG_ID}"
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
        1 * itemGroup.itemStash >> itemStash
        1 * itemStash.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        0 * _

        and:
        result == [
                itemGroup: itemGroup,
                itemStash: itemStash,
                userProfile: userProfile,
                ownGroup: Boolean.TRUE,
        ]
    }

    void "Test createItem()"() {
        given:
        Map linkGeneratorMap = ['action':SHOW_ACTION, 'id':1]
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addFile(multipartItemImage)
        controller.request.addFile(multipartSourceImage)
        controller.request.addHeader('referer', "${ITEM_GROUP_URI}/${LONG_ID}")

        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.createItem(itemCommand)

        then:
        1 * userService.currentUserId >> LONG_ID
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.name >> NAME
        1 * itemCommand.description >> DESCRIPTION
        1 * itemCommand.itemImage >> multipartItemImage
        1 * itemCommand.sourceImage >> multipartSourceImage
        1 * imageService.createImage(multipartItemImage, _ as String, _ as Map) >> itemImage
        1 * imageService.createImage(multipartSourceImage, _ as String, _ as Map) >> sourceImage
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemService.createItem(_ as Map) >> item
        1 * itemGroup.id >> LONG_ID
        1 * item.validate() >> Boolean.TRUE
        1 * item.id >> LONG_ID
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_URI}/${LONG_ID}"
        0 * _

        and:
        response.json.redirect == "${ITEM_URI}/${LONG_ID}"
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
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.itemStash >> itemStash
        1 * itemStash.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        0 * _

        and:
        result == [
                item: item,
                itemGroup: itemGroup,
                itemStash: itemStash,
                userProfile: userProfile,
                ownItem: Boolean.TRUE,
        ]
    }

    void "Test deleteItem()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE
        params.id = ONE

        when:
        controller.deleteItem()

        then:
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.itemStash >> itemStash
        1 * itemGroup.id >> LONG_ID
        1 * itemStash.user >> user
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        1 * itemService.deleteItem(item) >> Boolean.TRUE
        0 * _

        and:
        controller.modelAndView == null
        response.redirectedUrl == null
        response.json.redirect == "${ITEM_GROUP_URI}/${LONG_ID}"
    }

    void "Test deleteGroup()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE
        params.id = ONE

        when:
        controller.deleteGroup()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemGroup.itemStash >> itemStash
        1 * itemStash.id >> LONG_ID
        1 * itemStash.user >> user
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        1 * itemService.deleteGroup(itemGroup) >> Boolean.TRUE
        0 * _

        and:
        controller.modelAndView == null
        response.redirectedUrl == null
        response.json.redirect == "${ITEM_STASH_URI}/${LONG_ID}"
    }

    void "Test editItem()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE
        controller.request.addFile(multipartItemImage)
        controller.request.addFile(multipartSourceImage)
        controller.request.addHeader('referer', "${ITEM_URI}/${LONG_ID}")

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.name >> NAME
        1 * itemCommand.description >> DESCRIPTION
        1 * itemCommand.itemImage >> multipartItemImage
        1 * itemCommand.sourceImage >> multipartSourceImage
        1 * item.itemImageId >> ITEM_IMAGE_ID
        1 * item.sourceImageId >> SOURCE_IMAGE_ID
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        1 * imageService.replaceImage(ITEM_IMAGE_COLLECTION, ITEM_IMAGE_ID, multipartItemImage, _ as Map) >> itemImage
        1 * imageService.replaceImage(SOURCE_IMAGE_COLLECTION, SOURCE_IMAGE_ID, multipartSourceImage, _ as Map) >> sourceImage
        1 * itemImage.id >> ID
        1 * sourceImage.id >> ID
        1 * itemService.updateItem(item, _ as Map) >> updatedItem
        1 * updatedItem.validate() >> Boolean.TRUE
        1 * updatedItem.id >> LONG_ID
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
        response.json.redirect == "${ITEM_URI}/${LONG_ID}"
    }
}
