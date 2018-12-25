package club.tempvs.message

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String POST_METHOD = 'POST'
    private static final String PROFILE_HEADER = 'Profile'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_THREE = 3L
    private static final String COUNT_HEADER = 'X-Total-Count'

    def restCaller = Mock RestCaller
    def restResponse = Mock RestResponse
    def httpHeaders = Mock HttpHeaders

    def setup() {
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
        request.method = POST_METHOD
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
        request.method = POST_METHOD

        when:
        controller.readMessages(LONG_ONE)

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
}
