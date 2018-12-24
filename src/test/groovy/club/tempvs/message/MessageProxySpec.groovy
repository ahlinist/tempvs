package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.User
import grails.converters.JSON
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MessageProxySpec extends Specification {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L

    MessageProxy messageProxy

    def user = Mock User
    def restCaller = Mock RestCaller
    def jsonConverter = Mock JsonConverter
    def objectFactory = Mock ObjectFactory
    def profile = Mock Profile
    def initiator = Mock Profile
    def subject = Mock Profile
    def restResponse = Mock RestResponse
    def conversation = Mock Conversation
    def conversationList = Mock ConversationList
    def profileDto = Mock ProfileDto
    def addMessageDto = Mock AddMessagePayload
    def addParticipantPayload = Mock AddParticipantsPayload
    def updateConversationNamePayload = Mock UpdateConversationNamePayload

    def setup() {
        messageProxy = new MessageProxy(restCaller: restCaller, jsonConverter: jsonConverter, objectFactory: objectFactory)
    }

    def cleanup() {
    }

    void "test getConversations()"() {
        given:
        int page = 0
        int size = 20
        String jsonResponse = "{response}"
        Long profileId = 1L

        when:
        ConversationList result = messageProxy.getConversations(profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(ConversationList, jsonResponse) >> conversationList
        0 * _

        and:
        result == conversationList
    }

    void "test getConversations() for bad request"() {
        given:
        int page = 0
        int size = 20
        Long profileId = 1L

        when:
        messageProxy.getConversations(profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.INTERNAL_SERVER_ERROR
        1 * restResponse.responseBody
        0 * _

        and:
        thrown(RuntimeException)
    }

    void "test addMessage()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        String text = "message text"

        when:
        Conversation result = messageProxy.addMessage(conversationId, profile, text)

        then:
        1 * objectFactory.getInstance(ProfileDto, profile) >> profileDto
        1 * objectFactory.getInstance(AddMessagePayload, [author: profileDto, text: text]) >> addMessageDto
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversation
        0 * _

        and:
        result == conversation
    }

    void "test addParticipants()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        Map payloadMap = [initiator: profileDto, subjects: [profileDto]]

        when:
        Conversation result = messageProxy.addParticipants(conversationId, initiator, [subject])

        then:
        1 * objectFactory.getInstance(ProfileDto, initiator) >> profileDto
        1 * objectFactory.getInstance(ProfileDto, subject) >> profileDto
        1 * objectFactory.getInstance(AddParticipantsPayload, payloadMap) >> addParticipantPayload
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversation
        0 * _

        and:
        result == conversation
    }

    void "test removeParticipant()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L

        when:
        Conversation result = messageProxy.removeParticipant(conversationId, initiator, subject)

        then:
        1 * initiator.id >> LONG_ONE
        1 * subject.id >> LONG_TWO
        1 * restCaller.doDelete(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversation
        0 * _

        and:
        result == conversation
    }

    void "test updateConversationName()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        String conversationName = "conversation name"
        Map payloadMap = [name: conversationName, initiator: profileDto]

        when:
        Conversation result = messageProxy.updateConversationName(conversationId, initiator, conversationName)

        then:
        1 * objectFactory.getInstance(ProfileDto, initiator) >> profileDto
        1 * objectFactory.getInstance(UpdateConversationNamePayload, payloadMap) >> updateConversationNamePayload
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversation
        0 * _

        and:
        result == conversation
    }
}
