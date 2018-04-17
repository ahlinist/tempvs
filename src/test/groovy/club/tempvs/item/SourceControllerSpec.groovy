package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.communication.CommentService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageTagLib
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.periodization.Period
import club.tempvs.user.ProfileService
import club.tempvs.user.User
import club.tempvs.user.UserProfile
import club.tempvs.user.UserService
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

class SourceControllerSpec extends Specification implements ControllerUnitTest<SourceController> {

    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String NAME = 'name'
    private static final String TEXT = 'text'
    private static final String GET_METHOD = 'GET'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String FIELD_NAME = 'fieldName'
    private static final String SHOW_URI = '/source/show'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String PERIOD_URI = '/source/period'
    private static final String LIBRARY_URI = '/library'
    private static final String REPLACE_ACTION = 'replaceElement'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def source = Mock Source
    def comment = Mock Comment
    def period = Period.OTHER
    def userProfile = Mock UserProfile
    def imageUploadBean = Mock ImageUploadBean
    def imageUploadCommand = Mock ImageUploadCommand
    def multipartFile = Mock GrailsMockMultipartFile

    def userService = Mock UserService
    def imageTagLib = Mock ImageTagLib
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def profileService = Mock ProfileService
    def commentService = Mock CommentService
    def groovyPageRenderer = Mock PageRenderer
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.imageTagLib = imageTagLib
        controller.sourceService = sourceService
        controller.profileService = profileService
        controller.commentService = commentService
        controller.groovyPageRenderer = groovyPageRenderer
        controller.ajaxResponseHelper = ajaxResponseHelper
    }

    def cleanup() {
    }

    void "Test index()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.index()

        then:
        response.redirectedUrl.contains LIBRARY_URI
    }

    void "Test show()"() {
        when:
        def result = controller.show(LONG_ONE)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * source.period >> period
        1 * source.images >> [image]
        1 * userService.currentUserId >> LONG_TWO
        0 * _

        and:
        result == [source: source, period: period, images: [image], editAllowed: Boolean.TRUE]
    }

    void "Test createSource()"() {
        given:
        request.method = POST_METHOD
        controller.request.addHeader(REFERER, "${PERIOD_URI}/${period.id}")

        when:
        controller.createSource(source, imageUploadCommand)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * source.validate() >> Boolean.TRUE
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], SOURCE_COLLECTION) >> [image]
        1 * source.setImages([image])
        1 * sourceService.saveSource(source) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * source.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect("${SHOW_URI}/${LONG_ONE}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createSource() against invalid source"() {
        given:
        params.sourceId = ONE
        request.method = POST_METHOD

        when:
        controller.createSource(source, imageUploadCommand)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * source.validate() >> Boolean.TRUE
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], SOURCE_COLLECTION) >> [image]
        1 * source.setImages([image])
        1 * sourceService.saveSource(source) >> source
        1 * source.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(source) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createSource() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createSource(source, imageUploadCommand)

        then:
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * source.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(source) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test getSourcesByPeriod()"() {
        given:
        params.id = period.id

        when:
        controller.getSourcesByPeriod()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> {[source, source]}
        2 * source.id >> SourceControllerSpec.LONG_ONE
        2 * source.name >> NAME
        0 * _
    }

    void "Test editSourceField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editSourceField(LONG_ONE, FIELD_NAME, FIELD_VALUE)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * sourceService.editSourceField(source, FIELD_NAME, FIELD_VALUE) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
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
        1 * imageService.uploadImages([imageUploadBean], SOURCE_COLLECTION) >> [image]
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * source.addToImages(image)
        1 * sourceService.saveSource(source) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * source.images >> [image]
        1 * imageTagLib.modalCarousel(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteSource()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteSource(LONG_ONE)

        then: 'Successfully deleted'
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * source.period >> period
        1 * sourceService.deleteSource(source)
        1 * sourceService.getSourcesByPeriod(period) >> [source]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteImage()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteImage(LONG_ONE, LONG_TWO)

        then:
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * imageService.loadImage(LONG_TWO) >> image
        1 * sourceService.deleteImage(source, image) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * source.images >> [image]
        1 * imageTagLib.modalCarousel(_ as Map)
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
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * profileService.currentProfile >> userProfile
        1 * commentService.createComment(TEXT, userProfile) >> comment
        1 * comment.hasErrors() >> Boolean.FALSE
        1 * sourceService.addComment(source, comment) >> source
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
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * commentService.loadComment(LONG_TWO) >> comment
        1 * sourceService.deleteComment(source, comment) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }
}
