package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.ImageBean
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import com.tempvs.user.UserService
import grails.converters.JSON
import grails.test.mixin.TestFor
import grails.web.mapping.LinkGenerator
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ItemController)
class ItemControllerSpec extends Specification {

    private static final String ONE = '1'
    private static final Long LONG_ID = 1L
    private static final String REFERER = 'referer'
    private static final String SHOW_ACTION = 'show'
    private static final String GROUP_ACTION = 'group'
    private static final String ITEM_URI = '/item/show'
    private static final String PROPERTIES = 'properties'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String ITEM_STASH_URI = '/item/stash'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    def user = Mock(User)
    def json = Mock(JSON)
    def item = Mock(Item)
    def updatedItem = Mock(Item)
    def image = Mock(ImageBean)
    def itemGroup = Mock(ItemGroup)
    def userService = Mock(UserService)
    def userProfile = Mock(UserProfile)
    def itemService = Mock(ItemService)
    def itemCommand = Mock(ItemCommand)
    def grailsLinkGenerator = Mock(LinkGenerator)
    def ajaxResponseService = Mock(AjaxResponseService)
    def createItemGroupCommand = Mock(CreateItemGroupCommand)

    def setup() {
        controller.userService = userService
        controller.itemService = itemService
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
        when:
        params.id = ONE
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
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
        1 * user.itemGroups >> itemGroups
        0 * _

        and:
        result == [itemGroups: [itemGroup] as Set, user: user, userProfile: userProfile, editAllowed: true]
    }

    void "Test group creation against invalid command"() {
        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(createItemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation against invalid group"() {
        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful group creation"() {
        given:
        Map linkGeneratorMap = ['action':GROUP_ACTION, 'id':LONG_ID]
        controller.grailsLinkGenerator = grailsLinkGenerator

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.validate() >> Boolean.TRUE
        1 * itemGroup.getId() >> LONG_ID
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_GROUP_URI}/${LONG_ID}"
        1 * ajaxResponseService.renderRedirect("${ITEM_GROUP_URI}/${LONG_ID}") >> json
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
        1 * itemGroup.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
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
        controller.request.addHeader(REFERER, "${ITEM_GROUP_URI}/${LONG_ID}")
        params.groupId = ONE

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.createItem(_ as Item, _ as Map) >> item
        1 * item.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(item) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful createItem()"() {
        given:
        Map linkGeneratorMap = ['action':SHOW_ACTION, 'id':1]
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addHeader(REFERER, "${ITEM_GROUP_URI}/${LONG_ID}")
        params.groupId = ONE

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.createItem(_ as Item, _ as Map) >> item
        1 * item.validate() >> Boolean.TRUE
        1 * item.id >> LONG_ID
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> "${ITEM_URI}/${LONG_ID}"
        1 * ajaxResponseService.renderRedirect("${ITEM_URI}/${LONG_ID}") >> json
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
        1 * user.id >> LONG_ID
        1 * userService.currentUserId >> LONG_ID
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
        params.id = ONE

        when:
        controller.deleteItem()

        then: 'Successfully deleted'
        1 * itemService.getItem(ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemService.deleteItem(item) >> Boolean.TRUE
        1 * itemGroup.id >> LONG_ID
        1 * ajaxResponseService.renderRedirect("${ITEM_GROUP_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteGroup() against unexisting one"() {
        given:
        params.id = ONE

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

        when:
        controller.deleteGroup()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        1 * itemService.deleteGroup(itemGroup) >> Boolean.TRUE
        1 * ajaxResponseService.renderRedirect(ITEM_STASH_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItem() against invalid command"() {
        given:
        controller.request.addHeader(REFERER, "${ITEM_URI}/${LONG_ID}")
        params.itemId = ONE

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * itemCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(itemCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editItem() against invalid item"() {
        given:
        controller.request.addHeader(REFERER, "${ITEM_URI}/${LONG_ID}")
        params.itemId = ONE

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.updateItem(item, _ as Map) >> updatedItem
        1 * updatedItem.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(updatedItem) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test successful editItem()"() {
        given:
        controller.request.addHeader(REFERER, "${ITEM_URI}/${LONG_ID}")
        params.itemId = ONE

        when:
        controller.editItem(itemCommand)

        then:
        1 * itemService.getItem(ONE) >> item
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.getProperty(PROPERTIES) >> [:]
        1 * itemService.updateItem(item, _ as Map) >> updatedItem
        1 * updatedItem.validate() >> Boolean.TRUE
        1 * updatedItem.id >> LONG_ID
        1 * ajaxResponseService.renderRedirect("${ITEM_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
