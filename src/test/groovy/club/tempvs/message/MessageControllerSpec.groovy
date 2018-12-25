package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.User
import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String PROFILE_HEADER = 'Profile'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_THREE = 3L
    private static final String COUNT_HEADER = 'X-Total-Count'

    def json = Mock JSON
    def user = Mock User
    def profile = Mock Profile
    def profileService = Mock ProfileService
    def restCaller = Mock RestCaller
    def restResponse = Mock RestResponse
    def httpHeaders = Mock HttpHeaders
    def ajaxResponseHelper = Mock AjaxResponseHelper
    def profileDto = Mock ProfileDto
    def objectFactory = Mock ObjectFactory
    def readMessagesPayload = Mock ReadMessagesPayload

    def setup() {
        controller.profileService = profileService
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.objectFactory = objectFactory
        controller.restCaller = restCaller
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

        when:
        controller.loadMessages(id, page, size)

        then:
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test getNewConversationsCount()"() {
        given:
        Integer count = 5

        when:
        controller.getNewConversationsCount()

        then:
        1 * restCaller.doHead(_ as String, _) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(COUNT_HEADER) >> count
        1 * restResponse.statusCode >> HttpStatus.OK
        0 * _

        and:
        response.status == 200
        response.json.count == count
    }

    void "test createConversation()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createConversation()

        then:
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test loadConversations()"() {
        given:
        int page = 0
        int size = 20

        when:
        controller.loadConversations(page, size)

        then:
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test send()"() {
        given:
        request.method = POST_METHOD
        Long conversationId = 1L

        when:
        controller.send(conversationId)

        then:
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test addParticipants()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.addParticipants(LONG_ONE)

        then:
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test removeParticipant()"() {
        given:
        request.method = DELETE_METHOD
        params.subject = LONG_THREE

        when:
        controller.removeParticipant(LONG_ONE, LONG_THREE)

        then:
        1 * restCaller.doDelete(_ as String, _) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test updateConversationName() for remove"() {
        given:
        request.method = POST_METHOD

        when:
        controller.updateConversationName(LONG_ONE)

        then:
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
    }

    void "test readMessages()"() {
        given:
        List messageIds = [2, 3, 4]
        request.method = POST_METHOD
        request.json = [messageIds: messageIds]

        when:
        controller.readMessages(LONG_ONE)

        then:
        1 * profileService.currentProfile >> profile
        1 * objectFactory.getInstance(ProfileDto, profile) >> profileDto
        1 * objectFactory.getInstance(ReadMessagesPayload, [participant: profileDto, messageIds: messageIds]) >> readMessagesPayload
        1 * restCaller.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        0 * _

        and:
        response.status == 200
    }
}
