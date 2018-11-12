package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import grails.converters.JSON
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MessageProxySpec extends Specification {

    private static final String COUNT_HEADER = 'X-Total-Count'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L

    MessageProxy messageProxy

    def restCaller = Mock RestCaller
    def jsonConverter = Mock JsonConverter
    def objectFactory = Mock ObjectFactory
    def profile = Mock Profile
    def initiator = Mock Profile
    def subject = Mock Profile
    def restResponse = Mock RestResponse
    def conversationDto = Mock Conversation
    def httpHeaders = Mock HttpHeaders
    def participantDto = Mock Participant
    def createConversationDto = Mock CreateConversationPayload
    def addMessageDto = Mock AddMessagePayload
    def action = GroovyMock UpdateParticipantsPayload.Action
    def type = GroovyMock ProfileType
    def updateParticipantsPayload = Mock UpdateParticipantsPayload
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
        String result = messageProxy.getConversations(profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        0 * _

        and:
        result == jsonResponse
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
        0 * _

        and:
        thrown(RuntimeException)
    }

    void "test getNewConversationsCount()"() {
        given:
        Long profileId = 1L
        Integer newConversationsCount = 5

        when:
        Integer result = messageProxy.getNewConversationsCount(profile)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doHead(_ as String, _) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(COUNT_HEADER) >> newConversationsCount
        0 * _

        and:
        result == newConversationsCount
    }

    void "test getConversation()"() {
        given:
        int page = 0
        int size = 20
        String jsonResponse = "{response}"
        Long profileId = 1L
        Long conversationId = 2L

        when:
        Conversation result = messageProxy.getConversation(conversationId, profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }

    void "test createConversation()"() {
        given:
        String jsonResponse = "{response}"
        List<Profile> receivers = [profile]
        String text = "msg txt"
        String name = "conversation name"
        Long profileId = 1L
        String profileName = "profile name"

        when:
        Conversation result = messageProxy.createConversation(profile, receivers, text, name)

        then:
        2 * profile.id >> profileId
        2 * profile.toString() >> profileName
        2 * objectFactory.getInstance(Participant, [id: profileId, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(CreateConversationPayload, _ as Map) >> createConversationDto
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }

    void "test addMessage()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        Long profileId = 1L
        String profileName = "profile name"
        String text = "message text"

        when:
        Conversation result = messageProxy.addMessage(conversationId, profile, text)

        then:
        1 * profile.id >> profileId
        1 * profile.toString() >> profileName
        1 * objectFactory.getInstance(Participant, [id: profileId, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(AddMessagePayload, [author: participantDto, text: text]) >> addMessageDto
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }

    void "test updateParticipants()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        String profileName = "profile name"
        Map payloadMap = [initiator: participantDto, subject: participantDto, action: action]

        when:
        Conversation result = messageProxy.updateParticipants(conversationId, initiator, subject, action)

        then:
        1 * initiator.type >> type
        1 * subject.type >> type
        1 * initiator.id >> LONG_ONE
        1 * initiator.toString() >> profileName
        1 * subject.id >> LONG_TWO
        1 * subject.toString() >> profileName
        1 * objectFactory.getInstance(Participant, [id: LONG_ONE, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(Participant, [id: LONG_TWO, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(UpdateParticipantsPayload, payloadMap) >> updateParticipantsPayload
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }

    void "test updateConversationName()"() {
        given:
        String jsonResponse = "{response}"
        Long conversationId = 1L
        String profileName = "profile name"
        String conversationName = "conversation name"
        Map payloadMap = [name: conversationName, initiator: participantDto]

        when:
        Conversation result = messageProxy.updateConversationName(conversationId, initiator, conversationName)

        then:
        1 * initiator.id >> LONG_ONE
        1 * initiator.toString() >> profileName
        1 * objectFactory.getInstance(Participant, [id: LONG_ONE, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(UpdateConversationNamePayload, payloadMap) >> updateConversationNamePayload
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(Conversation, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }
}
