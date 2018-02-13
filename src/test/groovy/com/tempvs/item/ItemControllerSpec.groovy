package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.Comment
import com.tempvs.communication.CommentService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import com.tempvs.user.UserService
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class ItemControllerSpec extends Specification implements ControllerUnitTest<ItemController> {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String NAME = 'name'
    private static final String TEXT = 'text'
    private static final String POST_METHOD = 'POST'
    private static final String ITEM_URI = '/item/show'
    private static final String ITEM_COLLECTION = 'item'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String FIELD_NAME = 'fieldName'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_GROUP_URI = '/item/group'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    def user = Mock User
    def json = Mock JSON
    def item = Mock Item
    def image = Mock Image
    def type = GroovyMock Type
    def period = GroovyMock Period
    def source = Mock Source
    def comment = Mock Comment
    def itemGroup = Mock ItemGroup
    def userService = Mock UserService
    def userProfile = Mock UserProfile
    def itemService = Mock ItemService
    def item2Source = Mock Item2Source
    def itemCommand = Mock ItemCommand
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def commentService = Mock CommentService
    def groovyPageRenderer = Mock PageRenderer
    def imageUploadBean = Mock ImageUploadBean
    def itemGroupCommand = Mock ItemGroupCommand
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.itemService = itemService
        controller.imageService = imageService
        controller.sourceService = sourceService
        controller.commentService = commentService
        controller.groovyPageRenderer = groovyPageRenderer
        controller.ajaxResponseHelper = ajaxResponseHelper
    }

    def cleanup() {
    }

    void "Test stash() without id being logged in"() {
        when:
        def result = controller.stash()

        then:
        1 * userService.currentUser >> user
        1 * user.itemGroups >> [itemGroup]
        1 * user.userProfile >> userProfile
        0 * _

        and:
        result == [itemGroups: [itemGroup], user: user, userProfile: userProfile, editAllowed: Boolean.TRUE]
    }

    void "Test stash() with id being not logged in"() {
        when:
        def result = controller.stash(LONG_ONE)

        then:
        1 * userService.getUser(LONG_ONE) >> null
        0 * _

        and:
        !controller.modelAndView
        !response.redirectedUrl
        !result
    }

    void "Test stash() for success"() {
        when:
        def result = controller.stash(LONG_ONE)

        then:
        1 * userService.getUser(LONG_ONE) >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * user.itemGroups >> [itemGroup]
        0 * _

        and:
        result == [itemGroups: [itemGroup], user: user, userProfile: userProfile, editAllowed: true]
    }

    void "Test createGroup() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * userService.currentUser >> user
        1 * itemGroupCommand.setUser(user)
        1 * itemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(itemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createGroup() against invalid group"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * userService.currentUser >> user
        1 * itemGroupCommand.setUser(user)
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.name
        1 * itemGroupCommand.description
        1 * itemGroupCommand.errors
        1 * itemGroupCommand.user
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as ItemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createGroup()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createGroup(itemGroupCommand)

        then:
        1 * userService.currentUser >> user
        1 * itemGroupCommand.setUser(user)
        1 * itemGroupCommand.validate() >> Boolean.TRUE
        1 * itemGroupCommand.name >> NAME
        1 * itemGroupCommand.description
        1 * itemGroupCommand.errors
        1 * itemGroupCommand.user
        1 * itemService.createGroup(_ as ItemGroup) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.FALSE
        1 * itemGroup.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect("${ITEM_GROUP_URI}/${LONG_ONE}") >> json
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
        controller.group(LONG_ONE)

        then:
        1 * itemService.getGroup(LONG_ONE) >> null
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group()"() {
        given:
        Collection items = [item]

        when:
        def result = controller.group(LONG_ONE)

        then:
        1 * itemService.getGroup(LONG_ONE) >> itemGroup
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
        request.method = POST_METHOD

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(itemCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createItem() against invalid item"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.source
        1 * itemCommand.description >> DESCRIPTION
        1 * itemCommand.errors
        1 * itemCommand.itemGroup
        1 * itemCommand.type
        1 * itemCommand.period
        1 * itemCommand.name >> NAME
        2 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], ITEM_COLLECTION) >> [image]
        1 * itemService.updateItem(_ as Item, [image]) >> item
        1 * item.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as Item) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createItem()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createItem(itemCommand)

        then:
        1 * itemCommand.validate() >> Boolean.TRUE
        1 * itemCommand.source
        1 * itemCommand.description >> DESCRIPTION
        1 * itemCommand.errors
        1 * itemCommand.itemGroup
        1 * itemCommand.type
        1 * itemCommand.period >> period
        1 * itemCommand.name >> NAME
        2 * itemCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], ITEM_COLLECTION) >> [image]
        1 * itemService.updateItem(_ as Item, [image]) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * item.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect("${ITEM_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteImage()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteImage(LONG_ONE, LONG_TWO)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * imageService.getImage(LONG_TWO) >> image
        1 * itemService.deleteImage(item, image) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * item.images >> [image]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
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
        when:
        def result = controller.show(LONG_ONE)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * item.period >> period
        1 * item.type >> type
        1 * item.images >> [image]
        1 * itemGroup.user >> user
        1 * user.userProfile >> userProfile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * item.sources >> [source]
        1 * sourceService.getSourcesByPeriodAndType(period, type) >> [source]
        0 * _

        and:
        result == [
                user: user,
                item: item,
                itemGroup: itemGroup,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                images: [image],
                sources: [source],
                availableSources: [source],
        ]
    }

    void "Test deleteItem() against unexisting item"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteItem(LONG_ONE)

        then:
        1 * itemService.getItem(LONG_ONE) >> null
        1 * ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteItem()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteItem(LONG_ONE)

        then: 'Successfully deleted'
        1 * itemService.getItem(LONG_ONE) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemService.deleteItem(item)
        1 * itemGroup.items >> [item]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteGroup() against unexisting one"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteGroup(LONG_ONE)

        then:
        1 * itemService.getGroup(LONG_ONE) >> null
        1 * ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteGroup()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteGroup(LONG_ONE)

        then:
        1 * itemService.getGroup(LONG_ONE) >> itemGroup
        1 * itemGroup.user >> user
        1 * itemService.deleteGroup(itemGroup)
        1 * user.itemGroups >> [itemGroup]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test addImage()"() {
        given:
        params.itemId = LONG_ONE
        request.method = POST_METHOD

        when:
        controller.addImage(imageUploadBean)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * imageService.uploadImage(imageUploadBean, ITEM_COLLECTION) >> image
        1 * itemService.updateItem(item, [image]) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * item.images >> [image]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test editItemField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editItemField(LONG_ONE, FIELD_NAME, FIELD_VALUE)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * itemService.editItemField(item, FIELD_NAME, FIELD_VALUE) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test editItemGroupField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editItemGroupField(LONG_ONE, FIELD_NAME, FIELD_VALUE)

        then:
        1 * itemService.getGroup(LONG_ONE) >> itemGroup
        1 * itemService.editItemGroupField(itemGroup, FIELD_NAME, FIELD_VALUE) >> itemGroup
        1 * itemGroup.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test linkSource()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.linkSource(LONG_ONE, LONG_ONE)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * itemService.linkSource(item, source) >> item2Source
        1 * item2Source.hasErrors() >> Boolean.FALSE
        1 * item.period >> period
        1 * item.type >> type
        1 * item.sources >> [source]
        1 * sourceService.getSourcesByPeriodAndType(period, type) >> [source]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test unlinkSource()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.unlinkSource(LONG_ONE, LONG_ONE)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * itemService.unlinkSource(item, source)
        1 * item.period >> period
        1 * item.type >> type
        1 * item.sources >> [source]
        1 * sourceService.getSourcesByPeriodAndType(period, type) >> [source]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test addComment()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.addComment(LONG_ONE, TEXT)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * commentService.createComment(TEXT) >> comment
        1 * comment.hasErrors() >> Boolean.FALSE
        1 * itemService.addComment(item, comment) >> item
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.user >> user
        1 * user.id >> LONG_TWO
        1 * userService.currentUserId >> LONG_TWO
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteComment()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteComment(LONG_ONE, LONG_TWO)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * commentService.getComment(LONG_TWO) >> comment
        1 * itemService.deleteComment(item, comment) >> item
        1 * item.hasErrors() >> Boolean.FALSE
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.user >> user
        1 * user.id >> LONG_TWO
        1 * userService.currentUserId >> LONG_TWO
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }
}
