package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String POST_METHOD = 'POST'
    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_THREE = 3L

    def json = Mock JSON
    def profile = Mock Profile
    def conversation = Mock Conversation
    def conversationList = Mock ConversationList
    def profileService = Mock ProfileService
    def messageProxy = Mock MessageProxy
    def createConversationCommand = Mock CreateConversationCommand
    def ajaxResponseHelper = Mock AjaxResponseHelper
    def initiator = Mock Profile
    def subject = Mock Profile
    def objectFactory = Mock ObjectFactory
    def conversationWrapper = Mock ConversationWrapper

    def setup() {
        controller.profileService = profileService
        controller.messageProxy = messageProxy
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "test conversation()"() {
        when:
        controller.conversation()

        then:
        0 * _

        and:
        !controller.modelAndView
        !response.redirectedUrl
    }

    void "test conversation() with id"() {
        given:
        long conversationId = 1L

        when:
        def result = controller.conversation(conversationId)

        then:
        0 * _

        and:
        result == [conversationId: conversationId]
    }

    void "test loadMessages()"() {
        given:
        Long id = 1L
        int page = 0
        int size = 20
        params.page = page
        params.size = size
        Map argMap = [conversation: conversation, currentProfile: profile]

        when:
        controller.loadMessages(id)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversation(id, profile, page, size) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
        0 * _
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
        List<Profile> receivers = [receiver]

        when:
        controller.createDialogue(createConversationCommand)

        then:
        1 * createConversationCommand.validate() >> true
        1 * createConversationCommand.receivers >> receivers
        1 * receiver.id >> LONG_ONE
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_THREE
        1 * createConversationCommand.text >> text
        1 * messageProxy.createConversation(profile, [receiver], text, null) >> conversation
        1 * conversation.id >> conversationId
        1 * ajaxResponseHelper.renderRedirect("/message/conversation/" + conversationId) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "test createConversation()"() {
        given:
        request.method = POST_METHOD
        String text = "msg text"
        String name = "conversation name"
        def receiver = Mock Profile
        List<Profile> receivers = [receiver]
        Map argMap = [conversation: conversation, currentProfile: initiator]

        when:
        controller.createConversation(createConversationCommand)

        then:
        1 * createConversationCommand.validate() >> true
        1 * createConversationCommand.receivers >> receivers
        1 * receiver.id >> LONG_ONE
        1 * profileService.currentProfile >> initiator
        1 * initiator.id >> LONG_THREE
        1 * createConversationCommand.text >> text
        1 * createConversationCommand.name >> name
        1 * messageProxy.createConversation(initiator, [receiver], text, name) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
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
        1 * messageProxy.getConversations(profile, page, size) >> conversationList
        1 * conversationList.conversations
        0 * _
    }

    void "test send()"() {
        given:
        request.method = POST_METHOD
        Long conversationId = 1L
        String message = "msg text"
        params.message = message
        Map argMap = [conversation: conversation, currentProfile: profile]

        when:
        controller.send(conversationId)

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.addMessage(conversationId, profile, message) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
        0 * _
    }

    void "test addParticipant()"() {
        given:
        request.method = POST_METHOD
        params.subject = LONG_THREE
        Map argMap = [conversation: conversation, currentProfile: initiator]

        when:
        controller.addParticipant(LONG_ONE)

        then:
        1 * profileService.currentProfile >> initiator
        1 * profileService.getProfileById(LONG_THREE) >> subject
        1 * messageProxy.addParticipant(LONG_ONE, initiator, subject) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
        0 * _
    }

    void "test removeParticipant()"() {
        given:
        request.method = POST_METHOD
        params.subject = LONG_THREE
        Map argMap = [conversation: conversation, currentProfile: initiator]

        when:
        controller.removeParticipant(LONG_ONE)

        then:
        1 * profileService.currentProfile >> initiator
        1 * profileService.getProfileById(LONG_THREE) >> subject
        1 * messageProxy.removeParticipant(LONG_ONE, initiator, subject) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
        0 * _
    }

    void "test updateConversationName() for remove"() {
        given:
        request.method = POST_METHOD
        String conversationName = 'new name'
        params.conversationName = conversationName
        Map argMap = [conversation: conversation, currentProfile: initiator]

        when:
        controller.updateConversationName(LONG_ONE)

        then:
        1 * profileService.currentProfile >> initiator
        1 * messageProxy.updateConversationName(LONG_ONE, initiator, conversationName) >> conversation
        1 * objectFactory.getInstance(ConversationWrapper, argMap) >> conversationWrapper
        1 * conversationWrapper.conversation
        1 * conversationWrapper.currentProfile
        0 * _
    }
}
