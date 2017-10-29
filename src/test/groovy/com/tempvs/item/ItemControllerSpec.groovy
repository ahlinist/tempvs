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
    private static final String FIELD_NAME = 'fieldName'
    private static final String PROPERTIES = 'properties'
    private static final String FIELD_VALUE = 'fieldValue'
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
    def period = Period.XIX
    def source = Mock Source
    def itemGroup = Mock ItemGroup
    def userService = Mock UserService
    def userProfile = Mock UserProfile
    def itemService = Mock ItemService
    def itemCommand = Mock ItemCommand
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def imageUploadBean = Mock ImageUploadBean
    def itemGroupCommand = Mock ItemGroupCommand
    def grailsLinkGenerator = Mock LinkGenerator
    def itemImageUploadCommand = Mock ItemImageUploadCommand
    def ajaxResponseService = Mock AjaxResponseService

    def setup() {
        controller.userService = userService
        controller.itemService = itemService
        controller.imageService = imageService
        controller.sourceService = sourceService
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

    void "Test createGroup() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.getProperty(PROPERTIES) >> [:]
        1 * itemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createGroup() against invalid group"() {
        given:
        request.method = POST_METHOD
        Map parameters = [name: null]

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.getProperty(PROPERTIES) >> parameters
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.renderValidationResponse(_ as ItemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createGroup()"() {
        given:
        request.method = POST_METHOD
        Map properties = [name: NAME]
        request.addHeader(REFERER, "${ITEM_STASH_URI}/${ONE}")

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.getProperty(PROPERTIES) >> properties
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.FALSE
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

    void "Test group()"() {
        given:
        params.id = ONE
        Collection items = [item]

        when:
        def result = controller.group()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemGroup.user >> user
        1 * itemGroup.items >> items
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        0 * _

        and:
        result == [
                itemGroup: itemGroup,
                user: user,
                items: items,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]
    }

    void "Test createItem() against invalid command"() {
        given:
        params.groupId = ONE
        request.method = POST_METHOD

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.getProperty(PROPERTIES) >> [:]
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
        1 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * itemService.updateItem(_ as Item, [imageUploadBean]) >> item
        1 * item.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.renderValidationResponse(_ as Item) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createItem()"() {
        given:
        params.groupId = ONE
        request.method = POST_METHOD
        controller.grailsLinkGenerator = grailsLinkGenerator
        Map linkGeneratorMap = [uri: "${ITEM_GROUP_URI}/${LONG_ONE}"]
        controller.request.addHeader(REFERER, "${ITEM_GROUP_URI}/${LONG_ONE}")
        Map properties = [name: NAME, description: DESCRIPTION, period: period, imageUploadBeans: [imageUploadBean]]

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.getProperty(PROPERTIES) >> properties
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * itemService.updateItem(_ as Item, [imageUploadBean]) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_URI}/${LONG_ONE}"
        1 * ajaxResponseService.renderRedirect("${ITEM_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test addItemImages()"() {
        given:
        params.itemId = ONE
        request.method = POST_METHOD
        controller.request.addHeader(REFERER, "${EDIT_ITEM_PAGE_URI}/${ONE}")

        when:
        controller.addItemImages(itemImageUploadCommand)

        then:
        1 * itemImageUploadCommand.item >> item
        1 * itemImageUploadCommand.validate() >> Boolean.TRUE
        1 * item.hasErrors() >> Boolean.FALSE
        1 * itemImageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * itemService.updateItem(item,  [imageUploadBean]) >> item
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

    void "Test show() without id"() {
        when:
        controller.show()

        then:
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test show()"() {
        given:
        params.id = ONE

        when:
        def result = controller.show()

        then:
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * item.period >> period
        1 * itemGroup.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * sourceService.getSourcesByPeriod(period) >> [source]
        0 * _

        and:
        result == [
                item: item,
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                sources: [source]
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
        1 * itemService.deleteItem(item)
        1 * item.itemGroup >> itemGroup
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
        1 * itemService.deleteGroup(itemGroup)
        1 * ajaxResponseService.renderRedirect(ITEM_STASH_URI) >> json
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
        result == [item: item, itemGroup: itemGroup, user: user, userProfile: userProfile, editAllowed: Boolean.TRUE]
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
        1 * itemService.editItemImage(item, image, imageUploadBean) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseService.renderRedirect("${EDIT_ITEM_PAGE_URI}/${ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItemField()"() {
        given:
        params.objectId = ONE
        params.fieldName = FIELD_NAME
        params.fieldValue = FIELD_VALUE
        request.method = POST_METHOD

        when:
        controller.editItemField()

        then:
        1 * itemService.getItem(ONE) >> item
        1 * itemService.editItemField(item, FIELD_NAME, FIELD_VALUE) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.success == Boolean.TRUE
    }

    void "Test editItemGroupField()"() {
        given:
        params.objectId = ONE
        params.fieldName = FIELD_NAME
        params.fieldValue = FIELD_VALUE
        request.method = POST_METHOD


        when:
        controller.editItemGroupField()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemService.editItemGroupField(itemGroup, FIELD_NAME, FIELD_VALUE) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.success == Boolean.TRUE
    }
}
