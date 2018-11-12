package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import grails.converters.JSON
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

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

    String getConversations(Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&page=${page}&size=${size}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return response.responseBody
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Integer getNewConversationsCount(Profile profile) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&new=${true}"
        RestResponse response = restCaller.doHead(url, MESSAGE_SECURITY_TOKEN)
        return response.headers?.getFirst(COUNT_HEADER) as Integer
    }

    Conversation getConversation(long conversationId, Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/?page=${page}&size=${size}&caller=${profile.id}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Conversation createConversation(Profile author, List<Profile> receivers, String text, String name = null) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"

        Participant authorDto = objectFactory.getInstance(Participant, [id: author.id, name: author.toString()])

        List<Participant> receiverDtos = receivers.unique().collect { Profile profile ->
            objectFactory.getInstance(Participant, [id: profile.id, name: profile.toString()])
        }

        Map argumentMap = [author: authorDto, receivers: receiverDtos, text: text, name: name]
        CreateConversationPayload createConversationPayload = objectFactory.getInstance(CreateConversationPayload, argumentMap)

        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, createConversationPayload as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Conversation addMessage(Long conversationId, Profile author, String text) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/messages"
        Participant authorDto = objectFactory.getInstance(Participant, [id: author.id, name: author.toString()])
        Map argumentMap = [author: authorDto, text: text]
        AddMessagePayload addMessageDto = objectFactory.getInstance(AddMessagePayload, argumentMap)
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addMessageDto as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Conversation updateParticipants(Long conversationId, Profile initiator, Profile subject, UpdateParticipantsPayload.Action action) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/participants"

        if (initiator.type != subject.type) {
            throw new RuntimeException("Profiles can't be of different type.")
        }

        Participant initiatorDto = objectFactory.getInstance(Participant, [id: initiator.id, name: initiator.toString()])
        Participant subjectDto = objectFactory.getInstance(Participant, [id: subject.id, name: subject.toString()])
        UpdateParticipantsPayload updateParticipantsPayload =
                objectFactory.getInstance(UpdateParticipantsPayload, [initiator: initiatorDto, subject: subjectDto, action: action])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, updateParticipantsPayload as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }

    Conversation updateConversationName(Long conversationId, Profile initiator, String name) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/name"

        Participant initiatorDto = objectFactory.getInstance(Participant, [id: initiator.id, name: initiator.toString()])
        UpdateConversationNamePayload updateConversationNamePayload =
                objectFactory.getInstance(UpdateConversationNamePayload, [name: name, initiator: initiatorDto])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, updateConversationNamePayload as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            throw new RuntimeException("Response with code ${httpStatus.value()} has been returned.")
        }
    }
}
