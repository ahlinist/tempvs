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
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE = 'itemImage'
    private static final String SOURCE_IMAGE = 'sourceImage'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String ITEM_STASH_URI = '/item/stash'
    private static final String ITEM_URI = '/item/show'
    private static final String GROUP_ACTION = 'group'
    private static final String SHOW_ACTION = 'show'

    def user = Mock(User)
    def json = Mock(JSON)
    def item = Mock(Item)
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
    def createItemCommand = Mock(CreateItemCommand)
    def createItemGroupCommand = Mock(CreateItemGroupCommand)
    def grailsLinkGenerator = Mock(LinkGenerator)

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
        0 * _

        and:
        result == [itemStash: itemStash, userProfile: userProfile]
    }

    void "Test stash() with id for existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> itemStash
        1 * itemStash.user >> user
        1 * user.userProfile >> userProfile
        0 * _

        and:
        result == [itemStash: itemStash, userProfile: userProfile]
    }

    void "Test stash() with id for non-existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> null
        0 * _

        and:
        result == [itemStash: null, userProfile: null]
    }

    void "Test createGroup() rendering"() {
        when:
        controller.createGroup()

        then:
        controller.modelAndView == null
        !response.redirectedUrl
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

    void "Test createItem() rendering"() {
        when:
        controller.createItem()

        then:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test createItem()"() {
        given:
        Map linkGeneratorMap = ['action':SHOW_ACTION, 'id':1]
        def multipartItemImage = new MockMultipartFile(ITEM_IMAGE, "1234567" as byte[])
        def multipartSourceImage = new MockMultipartFile(SOURCE_IMAGE, "1234567" as byte[])
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addFile(multipartItemImage)
        controller.request.addFile(multipartSourceImage)
        controller.session.itemGroup = itemGroup

        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.createItem(createItemCommand)

        then:
        1 * userService.currentUserId >> LONG_ID
        1 * createItemCommand.validate() >> Boolean.TRUE
        1 * createItemCommand.getName() >> NAME
        1 * createItemCommand.getDescription() >> DESCRIPTION
        1 * createItemCommand.getItemImage() >> multipartItemImage
        1 * createItemCommand.getSourceImage() >> multipartSourceImage
        1 * imageService.createImage(_ as InputStream, _ as String, _ as Map) >> itemImage
        1 * imageService.createImage(_ as InputStream, _ as String, _ as Map) >> sourceImage
        1 * itemService.createItem(NAME, DESCRIPTION, itemImage, sourceImage, itemGroup) >> item
        1 * itemGroup.asType(ItemGroup) >> itemGroup
        1 * itemGroup.getId() >> LONG_ID
        1 * item.validate() >> Boolean.TRUE
        1 * item.getId() >> LONG_ID
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
}
