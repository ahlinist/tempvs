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

    private static final String POST_METHOD = 'POST'
    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final String APPEND_ACTION = 'appendElement'
    private static final String REPLACE_ACTION = 'replaceElement'

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

    void "test conversation()"() {
        given:
        long conversationId = 1L
        int page = 0
        int size = 20
        params.page = page
        params.size = size

        when:
        controller.conversation(conversationId)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversation(conversationId, profile, page, size) >> conversationDto
        0 * _

        and:
        controller.modelAndView.model == [conversation: conversationDto]
        view == '/message/conversation'
    }

    void "test loadMessages()"() {
        given:
        Long id = 1L
        int page = 0
        int size = 20
        params.page = page
        params.size = size

        when:
        controller.loadMessages(id)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversation(id, profile, page, size) >> conversationDto
        1 * groovyPageRenderer.render([template: '/message/templates/messages', model: [conversation: conversationDto]])
        0 * _

        and:
        response.json.action == REPLACE_ACTION
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
        params.page = page
        params.size = size

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

    void "test add"() {
        given:
        request.method = POST_METHOD
        Long conversationId = 1L
        String message = "msg text"
        params.message = message

        when:
        controller.add(conversationId)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.addMessage(conversationId, profile, message) >> conversationDto
        1 * groovyPageRenderer.render([template: '/message/templates/messages', model: [conversation: conversationDto]])
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }
}
