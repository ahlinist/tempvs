package club.tempvs.message

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpMethod
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final String MESSAGE_SERVICE_URL = System.getenv('MESSAGE_SERVICE_URL')
    private static final String MESSAGE_SECURITY_TOKEN = System.getenv('MESSAGE_SECURITY_TOKEN').encodeAsMD5() as String
    private static final String COUNT_HEADER = 'X-Total-Count'
    private static final String PROFILE_HEADER = 'Profile'

    static allowedMethods = [
            getNewConversationsCount: 'GET',
            loadConversations: 'GET',
            conversation: 'GET',
            createConversation: 'POST',
            add: 'POST',
            updateParticipants: 'POST',
            conversationName: 'POST',
            readMessages: 'POST',
            removeParticipant: 'POST',
    ]

    static defaultAction = 'conversation'

    RestCaller restCaller

    def conversation(Long id) {
        if (id != null) {
            [conversationId: id]
        }
    }

    def getNewConversationsCount() {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"
        RestResponse restResponse = restCaller.call(url, HttpMethod.HEAD, MESSAGE_SECURITY_TOKEN)
        Integer count = restResponse?.headers?.getFirst(COUNT_HEADER) as Integer
        render(status: restResponse?.statusCode?.value(), text: [count: count] as JSON)
    }

    def loadConversations(Integer page, Integer size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?page=${page}&size=${size}"
        RestResponse restResponse = restCaller.call(url, HttpMethod.GET, MESSAGE_SECURITY_TOKEN)
        buildResponse(restResponse)
    }

    def loadMessages(Long id, Integer page, Integer size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/?page=${page}&size=${size}"
        RestResponse restResponse = restCaller.call(url, HttpMethod.GET, MESSAGE_SECURITY_TOKEN)
        buildResponse(restResponse)
    }

    def createConversation() {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"
        RestResponse restResponse = restCaller.call(url, HttpMethod.POST, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        buildResponse(restResponse)
    }

    def send(Long id) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/messages"
        RestResponse restResponse = restCaller.call(url, HttpMethod.POST, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        buildResponse(restResponse)
    }

    def addParticipants(Long id) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/participants"
        RestResponse restResponse = restCaller.call(url, HttpMethod.POST, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        buildResponse(restResponse)
    }

    def removeParticipant(Long id, Long subject) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/participants/${subject}"
        RestResponse restResponse = restCaller.call(url, HttpMethod.DELETE, MESSAGE_SECURITY_TOKEN)
        buildResponse(restResponse)
    }

    def updateConversationName(Long id) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/name"
        RestResponse restResponse = restCaller.call(url, HttpMethod.POST, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        buildResponse(restResponse)
    }

    def readMessages(Long id) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/read"
        RestResponse restResponse = restCaller.call(url, HttpMethod.POST, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        buildResponse(restResponse)
    }

    private def buildResponse(RestResponse restResponse) {
        response.setHeader(PROFILE_HEADER, restResponse?.headers?.getFirst(PROFILE_HEADER))
        render(status: restResponse?.statusCode?.value(), text: restResponse?.responseBody)
    }
}
