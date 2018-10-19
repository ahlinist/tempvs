package club.tempvs.message

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String DISPLAY_COUNTER = 'displayCounter'

    def profile = Mock Profile
    def conversationsDto = Mock ConversationsDto
    def conversationDtoBean = Mock ConversationDtoBean
    def conversationDto = Mock ConversationDto
    def profileService = Mock ProfileService
    def messageProxy = Mock MessageProxy

    def setup() {
        controller.profileService = profileService
        controller.messageProxy = messageProxy
    }

    def cleanup() {
    }

    void "test index()"() {
        given:
        int page = 0
        int size = 20

        when:
        def result = controller.index()

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversations(profile, page, size) >> conversationsDto
        1 * conversationsDto.conversations >> [conversationDtoBean]
        0 * _

        and:
        result == [conversations: [conversationDtoBean]]
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

    void "test conversation()"() {
        given:
        long id = 1L
        int page = 0
        int size = 20

        when:
        controller.conversation(id)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversations(profile, page, size) >> conversationsDto
        1 * messageProxy.getConversation(id, profile, page, size) >> conversationDto
        1 * conversationsDto.conversations >> [conversationDtoBean]
        0 * _

        and:
        controller.modelAndView.model == [conversations: [conversationDtoBean], conversation: conversationDto]
        view == '/message/index'
    }
}
