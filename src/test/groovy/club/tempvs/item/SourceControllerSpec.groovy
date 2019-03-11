package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.communication.CommentService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SourceControllerSpec extends Specification implements ControllerUnitTest<SourceController> {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String TEXT = 'text'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String ROLE_SCRIBE = 'ROLE_SCRIBE'
    private static final String ROLE_CONTRIBUTOR = 'ROLE_CONTRIBUTOR'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def source = Mock Source
    def comment = Mock Comment
    def period = Period.OTHER
    def profile = Mock Profile

    def userService = Mock UserService
    def imageService = Mock ImageService
    def sourceService = Mock SourceService
    def commentService = Mock CommentService
    def groovyPageRenderer = Mock PageRenderer
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.sourceService = sourceService
        controller.commentService = commentService
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
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * imageService.getImage(LONG_TWO) >> image
        1 * sourceService.deleteImage(source, image) >> source
        1 * source.hasErrors() >> Boolean.FALSE
        1 * source.images >> [image]
        1 * userService.ifAnyRoleGranted(ROLE_SCRIBE)
        1 * userService.ifAnyRoleGranted(ROLE_CONTRIBUTOR)
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
        1 * sourceService.getSource(LONG_ONE) >> source
        1 * userService.currentProfile >> profile
        1 * commentService.createComment(TEXT, profile) >> comment
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
