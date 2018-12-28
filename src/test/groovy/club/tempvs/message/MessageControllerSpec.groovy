package club.tempvs.message

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    private static final String POST_METHOD = 'POST'
    private static final String URI = 'api/uri'
    private static final String PROFILE_HEADER = 'Profile'
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

    void "test updateConversationName() for remove"() {
        given:
        request.method = POST_METHOD

        when:
        controller.api(URI)

        then:
        1 * restCaller.call(_ as String, HttpMethod.POST, _, _ as JSON) >> restResponse
        1 * restResponse.headers >> httpHeaders
        1 * httpHeaders.getFirst(PROFILE_HEADER) >> "1"
        1 * httpHeaders.getFirst(COUNT_HEADER) >> "5"
        1 * restResponse.statusCode >> HttpStatus.OK
        1 * restResponse.responseBody
        0 * _

        and:
        response.status == 200
        response.header(COUNT_HEADER) == "5"
        response.header(PROFILE_HEADER) == "1"
    }
}
