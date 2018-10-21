package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException

@GrailsCompileStatic
class MessageProxy {

    private static final String MESSAGE_SERVICE_URL = System.getenv('MESSAGE_SERVICE_URL')
    private static final String MESSAGE_SECURITY_TOKEN = System.getenv('MESSAGE_SECURITY_TOKEN').encodeAsMD5() as String
    private static final String COUNT_HEADER = 'X-Total-Count'

    @Autowired
    RestCaller restCaller
    @Autowired
    JsonConverter jsonConverter
    @Autowired
    ObjectFactory objectFactory

    ConversationsDto getConversations(Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&page=${page}&size=${size}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationsDto, response.responseBody)
        } else {
            throw new AccessDeniedException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Integer getNewConversationsCount(Profile profile) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&new=${true}"
        RestResponse response = restCaller.doHead(url, MESSAGE_SECURITY_TOKEN)
        return response.headers?.getFirst(COUNT_HEADER) as Integer
    }

    ConversationDto getConversation(long conversationId, Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/?page=${page}&size=${size}&caller=${profile.id}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationDto, response.responseBody)
        } else {
            throw new AccessDeniedException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    ConversationDto createConversation(Profile author, List<Profile> receivers, String text, String name = null) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"

        ParticipantDto authorDto = objectFactory.getInstance(ParticipantDto, [id: author.id, name: author.toString()])

        List<ParticipantDto> receiverDtos = receivers.unique().collect { Profile profile ->
            objectFactory.getInstance(ParticipantDto, [id: profile.id, name: profile.toString()])
        }

        Map argumentMap = [author: authorDto, receivers: receiverDtos, text: text, name: name]
        CreateConversationDto createConversationDto = objectFactory.getInstance(CreateConversationDto, argumentMap)

        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, createConversationDto as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationDto, response.responseBody)
        } else {
            throw new AccessDeniedException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    ConversationDto addMessage(Long conversationId, Profile author, String text) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/messages"
        ParticipantDto authorDto = objectFactory.getInstance(ParticipantDto, [id: author.id, name: author.toString()])
        Map argumentMap = [author: authorDto, text: text, system: false]
        AddMessageDto addMessageDto = objectFactory.getInstance(AddMessageDto, argumentMap)
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addMessageDto as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationDto, response.responseBody)
        } else {
            throw new AccessDeniedException("Response with code ${httpStatus.value()} has been returned.")
        }
    }
}
