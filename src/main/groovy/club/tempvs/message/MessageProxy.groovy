package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
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

    @Autowired
    RestCaller restCaller
    @Autowired
    JsonConverter jsonConverter
    @Autowired
    ObjectFactory objectFactory

    ConversationList getConversations(Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&page=${page}&size=${size}"

        RestResponse response = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(ConversationList, response.responseBody)
        } else {
            log.error "Response status code ${httpStatus.value()}.\nMessage: ${response.responseBody}"
            throw new RuntimeException(String.valueOf(httpStatus.value()))
        }
    }

    Conversation addMessage(Long conversationId, Profile author, String text) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/messages"
        ProfileDto authorDto = objectFactory.getInstance(ProfileDto, author)
        Map argumentMap = [author: authorDto, text: text]
        AddMessagePayload addMessageDto = objectFactory.getInstance(AddMessagePayload, argumentMap)
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addMessageDto as JSON)
        HttpStatus httpStatus = response.statusCode

        if (httpStatus == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation addParticipants(Long conversationId, Profile initiator, List<Profile> subjects) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/participants"
        ProfileDto initiatorDto = objectFactory.getInstance(ProfileDto, initiator)

        List<ProfileDto> subjectDtos = subjects.collect { Profile subject ->
            objectFactory.getInstance(ProfileDto, subject)
        }

        AddParticipantsPayload addParticipantsPayload =
                objectFactory.getInstance(AddParticipantsPayload, [initiator: initiatorDto, subjects: subjectDtos])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, addParticipantsPayload as JSON)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else if (response.statusCode == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException(response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation removeParticipant(Long conversationId, Profile initiator, Profile subject) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/participants/${subject.id}?initiator=${initiator.id}"
        RestResponse response = restCaller.doDelete(url, MESSAGE_SECURITY_TOKEN)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(Conversation, response.responseBody)
        } else if (response.statusCode == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException(response.responseBody)
        } else {
            return processError(response)
        }
    }

    Conversation updateConversationName(Long conversationId, Profile initiator, String name) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${conversationId}/name"

        ProfileDto initiatorDto = objectFactory.getInstance(ProfileDto, initiator)
        UpdateConversationNamePayload updateConversationNamePayload =
                objectFactory.getInstance(UpdateConversationNamePayload, [name: name, initiator: initiatorDto])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, updateConversationNamePayload as JSON)

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
