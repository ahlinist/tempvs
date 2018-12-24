package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import groovy.util.logging.Slf4j
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0
    private static final Integer DEFAULT_PAGE_SIZE = 40
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
    ]

    static defaultAction = 'conversation'

    ProfileService profileService
    MessageProxy messageProxy
    AjaxResponseHelper ajaxResponseHelper
    LinkGenerator grailsLinkGenerator
    ObjectFactory objectFactory
    RestCaller restCaller

    def conversation(Long id) {
        if (id != null) {
            [conversationId: id]
        }
    }

    def getNewConversationsCount() {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"
        RestResponse restResponse = restCaller.doHead(url, MESSAGE_SECURITY_TOKEN)
        Integer count = restResponse.headers?.getFirst(COUNT_HEADER) as Integer
        render(status: restResponse.statusCode.value(), text: [count: count] as JSON)
    }

    def loadConversations(Integer page, Integer size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?page=${page}&size=${size}"
        RestResponse restResponse = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        response.setHeader(PROFILE_HEADER, restResponse.headers.getFirst(PROFILE_HEADER))
        render(status: restResponse.statusCode.value(), text: restResponse.responseBody)
    }

    def loadMessages(Long id, Integer page, Integer size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/?page=${page}&size=${size}"
        RestResponse restResponse = restCaller.doGet(url, MESSAGE_SECURITY_TOKEN)
        response.setHeader(PROFILE_HEADER, restResponse.headers.getFirst(PROFILE_HEADER))
        render(status: restResponse.statusCode.value(), text: restResponse.responseBody)
    }

    def createConversation() {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations"
        RestResponse restResponse = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        response.setHeader(PROFILE_HEADER, restResponse.headers.getFirst(PROFILE_HEADER))
        render(status: restResponse.statusCode.value(), text: restResponse.responseBody)
    }

    def send(Long id) {
        Profile currentProfile = profileService.currentProfile
        Conversation conversation = messageProxy.addMessage(id, currentProfile, params.message as String)
        render(objectFactory.getInstance(ConversationWrapper, conversation, currentProfile) as JSON)
    }

    def addParticipants(Long id, AddParticipantsCommand command) {
        Profile initiator = profileService.currentProfile
        List<Profile> subjects = command.participants
        Conversation conversation = messageProxy.addParticipants(id, initiator, subjects)
        render(objectFactory.getInstance(ConversationWrapper, conversation, initiator) as JSON)
    }

    def removeParticipant(Long id) {
        Profile initiator = profileService.currentProfile
        Profile subject = profileService.getProfileById(params.subject as Long)
        Conversation conversation = messageProxy.removeParticipant(id, initiator, subject)
        render(objectFactory.getInstance(ConversationWrapper, conversation, initiator) as JSON)
    }

    def updateConversationName(Long id) {
        String conversationName = params.conversationName as String
        Profile initiator = profileService.currentProfile
        Conversation conversation = messageProxy.updateConversationName(id, initiator, conversationName)
        render(objectFactory.getInstance(ConversationWrapper, conversation, initiator) as JSON)
    }

    def readMessages(Long id) {
        Profile currentProfile = profileService.currentProfile
        JSONObject json = request.JSON as JSONObject
        String url = "${MESSAGE_SERVICE_URL}/api/conversations/${id}/read"
        ProfileDto initiatorDto = objectFactory.getInstance(ProfileDto, currentProfile)
        ReadMessagesPayload readMessagesPayload =
                objectFactory.getInstance(ReadMessagesPayload, [participant: initiatorDto, messageIds: json.messageIds as List])
        RestResponse response = restCaller.doPost(url, MESSAGE_SECURITY_TOKEN, readMessagesPayload as JSON)
        render(status: response.statusCode.value())
    }

    def accessDeniedExceptionThrown(AccessDeniedException exception) {
        log.error exception.message
        return render(status: 403, text: 'An authorization error occurred')
    }

    def illegalArgumentExceptionThrown(IllegalArgumentException exception) {
        return render(status: 400, text: exception.message)
    }
}
