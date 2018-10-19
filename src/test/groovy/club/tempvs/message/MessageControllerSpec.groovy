package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final String APPEND_ACTION = 'appendElement'
    private static final String POST_METHOD = 'POST'

    def json = Mock JSON
    def profile = Mock Profile
    def conversationsDto = Mock ConversationsDto
    def conversationDtoBean = Mock ConversationDtoBean
    def conversationDto = Mock ConversationDto
    def profileService = Mock ProfileService
    def messageProxy = Mock MessageProxy
    def createDialogueCommand = Mock CreateDialogueCommand
    def ajaxResponseHelper = Mock AjaxResponseHelper
    def groovyPageRenderer = Mock PageRenderer

    def setup() {
        controller.profileService = profileService
        controller.messageProxy = messageProxy
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "test conversation() without id"() {
        when:
        controller.conversation()

        then:
        0 * _

        and:
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "test conversation()"() {
        given:
        Long id = 1L
        int page = 0
        int size = 20

        when:
        def result = controller.conversation(id)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversation(id, profile, page, size) >> conversationDto
        0 * _

        and:
        result == [conversation: conversationDto]
    }

    void "test getNewConversationsCount()"() {
        given:
        Integer count = 5

        when:
        controller.getNewConversationsCount()

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getNewConversationsCount(profile) >> count
        0 * _

        and:
        response.json.action == DISPLAY_COUNTER
        response.json.count == count
    }

    void "text createDialogue()"() {
        given:
        request.method = POST_METHOD
        String text = "msg text"
        Long conversationId = 1L
        def receiver = Mock Profile

        when:
        controller.createDialogue(createDialogueCommand)

        then:
        1 * createDialogueCommand.validate() >> true
        1 * createDialogueCommand.receiver >> receiver
        1 * createDialogueCommand.text >> text
        1 * profileService.currentProfile >> profile
        1 * messageProxy.createConversation(profile, [receiver], text) >> conversationDto
        1 * conversationDto.id >> conversationId
        1 * ajaxResponseHelper.renderRedirect("/message/conversation/" + conversationId) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "test loadConversations()"() {
        given:
        int page = 0
        int size = 20

        when:
        controller.loadConversations()

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversations(profile, page, size) >> conversationsDto
        1 * conversationsDto.conversations >> [conversationDtoBean]
        1 * groovyPageRenderer.render([template: '/message/templates/conversations', model: [conversations: [conversationDtoBean]]])
        0 * _

        and:
        response.json.action == APPEND_ACTION
    }
}
