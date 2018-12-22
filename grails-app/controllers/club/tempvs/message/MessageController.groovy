package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import groovy.util.logging.Slf4j
import org.grails.web.json.JSONObject
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final String SELF_SENT_MESSAGE = 'message.selfsent.error'
    private static final Integer DEFAULT_PAGE_NUMBER = 0
    private static final Integer DEFAULT_PAGE_SIZE = 40
    private static final String RECEIVERS_FIELD = 'receivers'

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

    def conversation(Long id) {
        if (id != null) {
            [conversationId: id]
        }
    }

    def getNewConversationsCount() {
        Profile currentProfile = profileService.currentProfile
        Integer count = messageProxy.getNewConversationsCount(currentProfile)
        render([count: count] as JSON)
    }

    def loadConversations() {
        Integer page = params.page as Integer ?: DEFAULT_PAGE_NUMBER
        Integer size = params.size as Integer ?: DEFAULT_PAGE_SIZE
        Profile currentProfile = profileService.currentProfile
        render(messageProxy.getConversations(currentProfile, page, size) as JSON)
    }

    def loadMessages(Long id) {
        Integer page = params.page as Integer ?: DEFAULT_PAGE_NUMBER
        Integer size = params.size as Integer ?: DEFAULT_PAGE_SIZE
        Profile currentProfile = profileService.currentProfile
        Conversation conversation = messageProxy.getConversation(id, currentProfile, page, size)
        render(objectFactory.getInstance(ConversationWrapper, conversation, currentProfile) as JSON)
    }

    def createConversation(CreateConversationCommand command) {
        Profile author = profileService.currentProfile
        List<Profile> receivers = command.receivers?.findAll()
        Conversation conversation = messageProxy.createConversation(author, receivers, command.text, command.name)
        render(objectFactory.getInstance(ConversationWrapper, conversation, author) as JSON)
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
        boolean result = messageProxy.readMessage(id, currentProfile, json.messageIds as List)
        render(status: result ? 200 : 400)
    }

    def accessDeniedExceptionThrown(AccessDeniedException exception) {
        log.error exception.message
        return render(status: 403, text: 'An authorization error occurred')
    }

    def illegalArgumentExceptionThrown(IllegalArgumentException exception) {
        return render(status: 400, text: exception.message)
    }
}
