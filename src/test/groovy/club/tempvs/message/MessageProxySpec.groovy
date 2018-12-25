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
    def profileDto = Mock ProfileDto
    def updateConversationNamePayload = Mock UpdateConversationNamePayload

    def setup() {
        messageProxy = new MessageProxy(restCaller: restCaller, jsonConverter: jsonConverter, objectFactory: objectFactory)
    }

    def cleanup() {
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
