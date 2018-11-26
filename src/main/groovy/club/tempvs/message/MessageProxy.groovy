package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import grails.converters.JSON
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

@Slf4j
@CompileStatic
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

    ConversationList getConversations(Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&page=${page}&size=${size}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN, profile.user.timeZone)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationList, response.responseBody)
        } else {
            log.error "Response status code ${httpStatus.value()}.\nMessage: ${response.responseBody}"
            throw new RuntimeException(String.valueOf(httpStatus.value()))
        }
    }

    Integer getNewConversationsCount(Profile profile) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&new=${true}"
        RestResponse response = restCaller.doHead(url, MESSAGE_SECURITY_TOKEN, profile.user.timeZone)
        return response.headers?.getFirst(COUNT_HEADER) as Integer
    }

    Conversation getConversation(Long conversationId, Profile profile, Integer page, Integer size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/?page=${page}&size=${size}&caller=${profile.id}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN, profile.user.timeZone)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation createConversation(Profile author, List<Profile> receivers, String text, String name) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"

        Participant authorDto = objectFactory.getInstance(Participant, [id: author.id, name: author.toString()])

        List<Participant> receiverDtos = receivers.unique().collect { Profile profile ->
            objectFactory.getInstance(Participant, [id: profile.id, name: profile.toString()])
        }

        Map argumentMap = [author: authorDto, receivers: receiverDtos, text: text, name: name]
        CreateConversationPayload createConversationPayload = objectFactory.getInstance(CreateConversationPayload, argumentMap)

        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, createConversationPayload as JSON, author.user.timeZone)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation addMessage(Long conversationId, Profile author, String text) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/messages"
        Participant authorDto = objectFactory.getInstance(Participant, [id: author.id, name: author.toString()])
        Map argumentMap = [author: authorDto, text: text]
        AddMessagePayload addMessageDto = objectFactory.getInstance(AddMessagePayload, argumentMap)
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addMessageDto as JSON, author.user.timeZone)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation addParticipants(Long conversationId, Profile initiator, List<Profile> subjects) {
        if (subjects.type.any { ProfileType type -> type != initiator.type}) {
            throw new RuntimeException("Profiles can't be of different type.")
        }

        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/participants"

        Participant initiatorDto = objectFactory.getInstance(Participant, [id: initiator.id, name: initiator.toString()])

        List<Participant> subjectDtos = subjects.collect { Profile subject ->
            objectFactory.getInstance(Participant, [id: subject.id, name: subject.toString()])
        }

        AddParticipantsPayload addParticipantsPayload =
                objectFactory.getInstance(AddParticipantsPayload, [initiator: initiatorDto, subjects: subjectDtos])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addParticipantsPayload as JSON, initiator.user.timeZone)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation removeParticipant(Long conversationId, Profile initiator, Profile subject) {
        if (initiator.type != subject.type) {
            throw new RuntimeException("Profiles can't be of different type.")
        }

        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/participants/${subject.id}?initiator=${initiator.id}"
        RestResponse response = restCaller.doDelete(url, MESSAGE_SECURITY_TOKEN, initiator.user.timeZone)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation updateConversationName(Long conversationId, Profile initiator, String name) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/name"

        Participant initiatorDto = objectFactory.getInstance(Participant, [id: initiator.id, name: initiator.toString()])
        UpdateConversationNamePayload updateConversationNamePayload =
                objectFactory.getInstance(UpdateConversationNamePayload, [name: name, initiator: initiatorDto])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, updateConversationNamePayload as JSON, initiator.user.timeZone)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    private Conversation processError(RestResponse response) {
        HttpStatus httpStatus = response.statusCode
        String message = "Response status code ${httpStatus.value()}.\nMessage: ${response.responseBody}"
        log.error message
        throw new RuntimeException(message)
    }
}
