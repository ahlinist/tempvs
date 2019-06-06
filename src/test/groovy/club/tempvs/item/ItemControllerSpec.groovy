package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

class ItemControllerSpec extends Specification implements ControllerUnitTest<ItemController> {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String TEXT = 'text'
    private static final String POST_METHOD = 'POST'
    private static final String ITEM_URI = '/item/show'
    private static final String ITEM_COLLECTION = 'item'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String FIELD_NAME = 'fieldName'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'

    def user = Mock User
    def json = Mock JSON
    def item = Mock Item
    def image = Mock Image
    def source = Mock Source
    def comment = Mock Comment
    def period = GroovyMock Period
    def itemGroup = Mock ItemGroup
    def profile = Mock Profile
    def item2Source = Mock Item2Source
    def itemType = GroovyMock ItemType
    def sourceType = GroovyMock SourceType
    def imageUploadBean = Mock ImageUploadBean
    def multipartFile = Mock GrailsMockMultipartFile

    def itemService = Mock ItemService
    def userService = Mock UserService
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def groovyPageRenderer = Mock PageRenderer
    def imageUploadCommand = Mock ImageUploadCommand
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.itemService = itemService
        controller.imageService = imageService
        controller.sourceService = sourceService
        controller.groovyPageRenderer = groovyPageRenderer
        controller.ajaxResponseHelper = ajaxResponseHelper
    }

    def cleanup() {
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
        1 * item.itemType >> itemType
        1 * item.images >> [image]
        1 * itemGroup.user >> user
        1 * user.userProfile >> profile
        1 * user.id >> LONG_ONE
        1 * userService.currentUserId >> LONG_ONE
        1 * item.sources >> [source]
        1 * source.sourceType >> sourceType
        1 * sourceService.getSourcesByPeriodAndItemType(period, itemType, [source]) >> [source]
        0 * _

        and:
        result == [
                user: user,
                item: item,
                itemGroup: itemGroup,
                profile: profile,
                editAllowed: Boolean.TRUE,
                images: [image],
                sources: [source],
                sourceMap: [(sourceType): [source]],
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
        1 * itemService.deleteItem(item)
        1 * item.itemGroup >> itemGroup
        1 * itemGroup.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect(_ as String) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test addImages()"() {
        given:
        params.objectId = LONG_ONE
        request.method = POST_METHOD

        when:
        controller.addImages(imageUploadCommand)

        then:
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageUploadBean.image >> multipartFile
        1 * multipartFile.empty >> Boolean.FALSE
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * imageService.uploadImages([imageUploadBean], ITEM_COLLECTION) >> [image]
        1 * itemService.getItem(LONG_ONE) >> item
        1 * item.addToImages(image)
        1 * itemService.saveItem(item) >> item
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

    void "Test linkSource()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.linkSource(LONG_ONE, LONG_ONE)

        then:
        1 * itemService.getItem(LONG_ONE) >> item
        1 * sourceService.loadSource(LONG_ONE) >> source
        1 * itemService.linkSource(item, source) >> item2Source
        1 * item2Source.hasErrors() >> Boolean.FALSE
        1 * item.period >> period
        1 * item.itemType >> itemType
        1 * item.sources >> [source]
        1 * source.sourceType >> sourceType
        1 * sourceService.getSourcesByPeriodAndItemType(period, itemType, [source]) >> [source]
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
        1 * sourceService.loadSource(LONG_ONE) >> source
        1 * itemService.unlinkSource(item, source)
        1 * item.period >> period
        1 * item.itemType >> itemType
        1 * item.sources >> [source]
        1 * source.sourceType >> sourceType
        1 * sourceService.getSourcesByPeriodAndItemType(period, itemType, [source]) >> [source]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }
}
