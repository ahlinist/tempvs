package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import com.tempvs.user.UserService
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.web.mapping.LinkGenerator
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([Item, ItemGroup])
@TestFor(ItemController)
class ItemControllerSpec extends Specification {

    private static final String ONE = '1'
    private static final String TWO = '2'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String NAME = 'name'
    private static final String AUTH_URI = '/auth'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String ITEM_URI = '/item/show'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String PROPERTIES = 'properties'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String ITEM_STASH_URI = '/item/stash'
    private static final String EDIT_ITEM_PAGE_URI = '/item/editItemPage'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    def user = Mock User
    def json = Mock JSON
    def item = Mock Item
    def image = Mock Image
    def itemGroup = Mock ItemGroup
    def period = Period.valueOf'XIX'
    def userService = Mock UserService
    def userProfile = Mock UserProfile
    def itemService = Mock ItemService
    def itemCommand = Mock ItemCommand
    def imageService = Mock ImageService
    def imageUploadBean = Mock ImageUploadBean
    def itemGroupCommand = Mock ItemGroupCommand
    def grailsLinkGenerator = Mock LinkGenerator
    def itemImageUploadCommand = Mock ItemImageUploadCommand
    def ajaxResponseService = Mock AjaxResponseService

    def setup() {
        controller.userService = userService
        controller.itemService = itemService
        controller.imageService = imageService
        controller.ajaxResponseService = ajaxResponseService
    }

    def cleanup() {
    }

    void "Test stash() without id being logged in"() {
        given:
        Set itemGroups = [itemGroup]

        when:
        def result = controller.stash()

        then:
        1 * userService.currentUser >> user
        1 * user.itemGroups >> itemGroups
        1 * user.userProfile >> userProfile
        0 * _

        and:
        result == [itemGroups: [itemGroup] as Set, user: user, userProfile: userProfile, editAllowed: Boolean.TRUE]
    }

    void "Test stash() with id being not logged in"() {
        given:
        params.id = ONE

        when:
        def result = controller.stash()

        then:
        1 * userService.getUser(ONE) >> null
        0 * _

        and:
        !controller.modelAndView
        !response.redirectedUrl
        !result
    }

    void "Test stash() for success"() {
        given:
        params.id = ONE
        Set itemGroups = [itemGroup]

        when:
        def result = controller.stash()

        then:
        1 * userService.getUser(ONE) >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * user.itemGroups >> itemGroups
        0 * _

        and:
        result == [itemGroups: [itemGroup] as Set, user: user, userProfile: userProfile, editAllowed: true]
    }

    void "Test group creation against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation against invalid group"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.getProperty(PROPERTIES) >> [:]
        1 * userService.currentUser >> user
        1 * ajaxResponseService.renderValidationResponse(_ as ItemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful group creation"() {
        given:
        request.method = POST_METHOD
        Map properties = [name: NAME]
        request.addHeader(REFERER, "${ITEM_STASH_URI}/${ONE}")

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.getProperty(PROPERTIES) >> properties
        1 * userService.currentUser >> user
        1 * itemService.saveGroup(_ as ItemGroup) >> itemGroup
        1 * ajaxResponseService.renderRedirect("${ITEM_STASH_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
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
        given:
        params.id = ONE

        when:
        controller.group()

        then:
        1 * itemService.getGroup(ONE) >> null
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group() for existing id"() {
        given:
        params.id = ONE

        when:
        def result = controller.group()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemGroup.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        0 * _

        and:
        result == [
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]
    }

    void "Test createItem() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createItem() against invalid item"() {
        given:
        params.groupId = ONE
        request.method = POST_METHOD
        Map properties = [name: NAME, description: DESCRIPTION, imageUploadBeans: [imageUploadBean]]

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.getProperty(PROPERTIES) >> properties
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * ajaxResponseService.renderValidationResponse(_ as Item) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful createItem()"() {
        given:
        params.groupId = ONE
        request.method = POST_METHOD
        List<Image> images = [image]
        controller.grailsLinkGenerator = grailsLinkGenerator
        Map linkGeneratorMap = [uri: "${ITEM_GROUP_URI}/${LONG_ONE}"]
        controller.request.addHeader(REFERER, "${ITEM_GROUP_URI}/${LONG_ONE}")
        Map properties = [name: NAME, description: DESCRIPTION, period: period, imageUploadBeans: [imageUploadBean]]

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.getProperty(PROPERTIES) >> properties
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], ITEM_IMAGE_COLLECTION) >> images
        1 * itemService.saveItem(_ as Item, images) >> item
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_URI}/${LONG_ONE}"
        1 * ajaxResponseService.renderRedirect("${ITEM_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
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
        1 * itemGroup.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        0 * _

        and:
        result == [
                item: item,
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]
    }

    void "Test deleteItem() against unexisting item"() {
        given:
        request.method = DELETE_METHOD
        params.id = ONE

        when:
        controller.deleteItem()

        then:
        1 * itemService.getItem(ONE) >> null
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteItem()"() {
        given:
        request.method = DELETE_METHOD
        params.id = ONE

        when:
        controller.deleteItem()

        then: 'Successfully deleted'
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemService.deleteItem(item) >> Boolean.TRUE
        1 * itemGroup.id >> LONG_ONE
        1 * ajaxResponseService.renderRedirect("${ITEM_GROUP_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteGroup() against unexisting one"() {
        given:
        params.id = ONE
        request.method = DELETE_METHOD

        when:
        controller.deleteGroup()

        then:
        1 * itemService.getGroup(ONE) >> null
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteGroup()"() {
        given:
        params.id = ONE
        request.method = DELETE_METHOD

        when:
        controller.deleteGroup()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemService.deleteGroup(itemGroup) >> Boolean.TRUE
        1 * ajaxResponseService.renderRedirect(ITEM_STASH_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editGroup()"() {
        given:
        params.groupId = ONE
        request.method = POST_METHOD
        Map properties = [name: NAME, description: DESCRIPTION]
        controller.request.addHeader(REFERER, "${ITEM_GROUP_URI}/${ONE}")

        when:
        controller.editGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.getProperty(PROPERTIES) >> properties
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * itemGroup.asType(ItemGroup) >> itemGroup
        1 * itemGroup.validate() >> Boolean.TRUE
        1 * itemService.saveGroup(itemGroup) >> itemGroup
        1 * ajaxResponseService.renderRedirect("${ITEM_GROUP_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItem() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItem() against invalid item"() {
        given:
        params.itemId = ONE
        request.method = POST_METHOD
        Map properties = [name: NAME, description: DESCRIPTION]

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.getProperty(PROPERTIES) >> properties
        1 * itemService.getItem(ONE) >> item
        1 * item.asType(Item) >> item
        1 * item.setName(NAME)
        1 * item.setDescription(DESCRIPTION)
        1 * item.validate()
        1 * ajaxResponseService.renderValidationResponse(item) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful editItem()"() {
        given:
        params.itemId = ONE
        request.method = POST_METHOD
        Map properties = [name: NAME, description: DESCRIPTION, period: period, imageUploadBeans: [imageUploadBean]]
        controller.request.addHeader(REFERER, "${ITEM_URI}/${LONG_ONE}")

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemService.getItem(ONE) >> item
        1 * itemCommand.getProperty(PROPERTIES) >> properties
        1 * item.setName(NAME)
        1 * item.setDescription(DESCRIPTION)
        1 * item.setPeriod(period)
        1 * item.asType(Item) >> item
        1 * item.validate() >> Boolean.TRUE
        1 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], ITEM_IMAGE_COLLECTION) >> [image]
        1 * itemService.saveItem(item, [image]) >> item
        1 * ajaxResponseService.renderRedirect("${ITEM_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItemPage()"() {
        given:
        params.id = ONE

        when:
        def result = controller.editItemPage()

        then:
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.user >> user
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * user.userProfile >> userProfile
        0 * _

        and:
        result == [item: item, itemGroup: itemGroup, user: user, userProfile: userProfile]
    }

    void "Test unauthorized editItemPage()"() {
        given:
        params.id = ONE

        when:
        def result = controller.editItemPage()

        then:
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.user >> user
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_TWO
        0 * _

        and:
        response.redirectedUrl == AUTH_URI
        !result
    }

    void "Test addImagesToItem()"() {
        given:
        params.itemId = ONE
        request.method = POST_METHOD
        controller.request.addHeader(REFERER, "${EDIT_ITEM_PAGE_URI}/${ONE}")

        when:
        controller.addItemImages(itemImageUploadCommand)

        then:
        1 * itemImageUploadCommand.validate() >> Boolean.TRUE
        1 * itemService.getItem(ONE) >> item
        1 * item.asType(Item) >> item
        1 * item.validate() >> Boolean.TRUE
        1 * itemImageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], ITEM_IMAGE_COLLECTION) >> [image]
        1 * itemService.saveItem(item, [image]) >> item
        1 * ajaxResponseService.renderRedirect("${EDIT_ITEM_PAGE_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteItemImage()"() {
        given:
        request.method = DELETE_METHOD
        request.addHeader(REFERER, "${EDIT_ITEM_PAGE_URI}/${ONE}")

        when:
        controller.deleteItemImage(ONE, TWO)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * imageService.getImage(TWO) >> image
        1 * itemService.deleteItemImage(item, image)
        1 * ajaxResponseService.renderRedirect("${EDIT_ITEM_PAGE_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItemImage()"() {
        given:
        params.itemId = ONE
        params.imageId = TWO
        request.method = POST_METHOD
        request.addHeader(REFERER, "${EDIT_ITEM_PAGE_URI}/${ONE}")

        when:
        controller.editItemImage(imageUploadBean)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * imageService.getImage(TWO) >> image
        1 * image.collection >> ITEM_IMAGE_COLLECTION
        1 * imageService.updateImage(imageUploadBean, ITEM_IMAGE_COLLECTION, image) >> image
        1 * itemService.saveItem(item) >> item
        1 * ajaxResponseService.renderRedirect("${EDIT_ITEM_PAGE_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
