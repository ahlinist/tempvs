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
