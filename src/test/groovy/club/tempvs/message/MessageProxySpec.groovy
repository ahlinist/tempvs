package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.lang.Specification

class MessageProxySpec extends Specification {

    private static final String COUNT_HEADER = 'X-Total-Count'

    MessageProxy messageProxy

    def restCaller = Mock RestCaller
    def jsonConverter = Mock JsonConverter
    def objectFactory = Mock ObjectFactory
    def profile = Mock Profile
    def restResponse = Mock RestResponse
    def conversationsDto = Mock ConversationsDto
    def httpHeaders = Mock HttpHeaders

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
        String jsonResponse = "{response}"
        Long profileId = 1L

        when:
        messageProxy.getConversations(profile, page, size)

        then:
        1 * profile.id >> profileId
        1 * restCaller.doGet(_ as String, _) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.INTERNAL_SERVER_ERROR
        1 * objectFactory.getInstance(ConversationsDto) >> conversationsDto
        0 * _
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
}
