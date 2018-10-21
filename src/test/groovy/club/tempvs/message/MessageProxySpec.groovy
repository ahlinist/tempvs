package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import grails.converters.JSON
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

class MessageProxySpec extends Specification {

    private static final String COUNT_HEADER = 'X-Total-Count'

    MessageProxy messageProxy

    def restCaller = Mock RestCaller
    def jsonConverter = Mock JsonConverter
    def objectFactory = Mock ObjectFactory
    def profile = Mock Profile
    def restResponse = Mock RestResponse
    def conversationDto = Mock ConversationDto
    def conversationsDto = Mock ConversationsDto
    def httpHeaders = Mock HttpHeaders
    def participantDto = Mock ParticipantDto
    def createConversationDto = Mock CreateConversationDto
    def addMessageDto = Mock AddMessageDto

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
        ConversationsDto result = messageProxy.getConversations(profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(ConversationsDto, jsonResponse) >> conversationsDto
        0 * _

        and:
        result == conversationsDto
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
        thrown(AccessDeniedException)
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
        ConversationDto result = messageProxy.getConversation(conversationId, profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(ConversationDto, jsonResponse) >> conversationDto
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
        ConversationDto result = messageProxy.createConversation(profile, receivers, text, name)

        then:
        2 * profile.id >> profileId
        2 * profile.toString() >> profileName
        2 * objectFactory.getInstance(ParticipantDto, [id: profileId, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(CreateConversationDto, _ as Map) >> createConversationDto
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(ConversationDto, jsonResponse) >> conversationDto
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
        ConversationDto result = messageProxy.addMessage(conversationId, profile, text)

        then:
        1 * profile.id >> profileId
        1 * profile.toString() >> profileName
        1 * objectFactory.getInstance(ParticipantDto, [id: profileId, name: profileName]) >> participantDto
        1 * objectFactory.getInstance(AddMessageDto, [author: participantDto, text: text]) >> addMessageDto
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody >> jsonResponse
        1 * jsonConverter.convert(ConversationDto, jsonResponse) >> conversationDto
        0 * _

        and:
        result == conversationDto
    }
}
