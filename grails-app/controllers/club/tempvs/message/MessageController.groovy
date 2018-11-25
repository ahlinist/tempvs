package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import groovy.util.logging.Slf4j
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final String SELF_SENT_MESSAGE = 'message.selfsent.error'
    private static final String MULTIPLE_RECEIVERS = 'dialogue.multiple.receivers.error'
    private static final Integer DEFAULT_PAGE_NUMBER = 0
    private static final Integer DEFAULT_PAGE_SIZE = 40
    private static final String RECEIVERS_FIELD = 'receivers'

    static allowedMethods = [
            getNewConversationsCount: 'GET',
            loadConversations: 'GET',
            conversation: 'GET',
            createDialogue: 'POST',
            add: 'POST',
            updateParticipants: 'POST',
            conversationName: 'POST',
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

    def createDialogue(CreateConversationCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        Profile author = profileService.currentProfile
        List<Profile> receivers = command.receivers.findAll()

        if (receivers.size() > 1) {
            return render(ajaxResponseHelper.renderFormMessage(false, MULTIPLE_RECEIVERS))
        }

        if (receivers.find { Profile profile -> profile.id == author.id}) {
            return render(ajaxResponseHelper.renderFormMessage(false, SELF_SENT_MESSAGE))
        }

        Conversation conversation = messageProxy.createConversation(author, receivers, command.text, null)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'message', action: 'conversation', id: conversation.id))
    }

    def createConversation(CreateConversationCommand command) {
        if (!command.validate()) {
            return render(status: 400, text: ajaxResponseHelper.renderErrors(command))
        }

        Profile author = profileService.currentProfile
        List<Profile> receivers = command.receivers.findAll().unique()

        if (receivers.find { Profile profile -> profile.id == author.id}) {
            List errorList = [[name: RECEIVERS_FIELD, message: SELF_SENT_MESSAGE]]
            return render(status: 400, text: ajaxResponseHelper.renderErrors(errorList))
        }

        Conversation conversation = messageProxy.createConversation(author, receivers, command.text, command.name)
        render(objectFactory.getInstance(ConversationWrapper, conversation, author) as JSON)
    }

    def send(Long id) {
        Profile currentProfile = profileService.currentProfile
        Conversation conversation = messageProxy.addMessage(id, currentProfile, params.message as String)
        render(objectFactory.getInstance(ConversationWrapper, conversation, currentProfile) as JSON)
    }

    def addParticipants(Long id, AddParticipantsCommand command) {
        if (!command.validate()) {
            return render(status: 400, text: ajaxResponseHelper.renderErrors(command))
        }

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

    def accessDeniedExceptionThrown(AccessDeniedException exception) {
        log.error exception.message
        return render(status: 403, text: 'An authorization error occurred')
    }
}
