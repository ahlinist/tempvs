package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import groovy.util.logging.Slf4j
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final String SELF_SENT_MESSAGE = 'message.selfsent.error'
    private static final String MULTIPLE_RECEIVERS = 'dialogue.multiple.receivers.error'
    private static final String REPLACE_ACTION = 'replaceElement'
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
    PageRenderer groovyPageRenderer

    def conversation(Long id) {
        if (id) {
            Integer page = params.page as Integer ?: DEFAULT_PAGE_NUMBER
            Integer size = params.size as Integer ?: DEFAULT_PAGE_SIZE
            Profile currentProfile = profileService.currentProfile
            Conversation conversation = messageProxy.getConversation(id, currentProfile, page, size)
            render view: '/message/conversation', model: [conversation: conversation, currentProfile: currentProfile]
        }
    }

    def getNewConversationsCount() {
        Profile currentProfile = profileService.currentProfile
        Integer count = messageProxy.getNewConversationsCount(currentProfile)
        render([action: DISPLAY_COUNTER, count: count] as JSON)
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
        Map model = [conversation: conversation, currentProfile: currentProfile]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
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
        Map model = [conversation: conversation, currentProfile: author]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([template: template] as JSON)
    }

    def send(Long id) {
        Profile currentProfile = profileService.currentProfile
        Conversation conversation = messageProxy.addMessage(id, currentProfile, params.message as String)
        Map model = [conversation: conversation, currentProfile: currentProfile]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([template: template] as JSON)
    }

    def updateParticipants(Long id) {
        String actionString = params.updateAction as String
        Profile initiator = profileService.currentProfile
        Profile subject = profileService.getProfileById(params.subject as Long)
        UpdateParticipantsPayload.Action action = UpdateParticipantsPayload.Action.valueOf(actionString.toUpperCase())
        Conversation conversation = messageProxy.updateParticipants(id, initiator, subject, action)
        Map model = [conversation: conversation, currentProfile: initiator]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([template: template] as JSON)
    }

    def updateConversationName(Long id) {
        String conversationName = params.conversationName as String
        Profile initiator = profileService.currentProfile
        Conversation conversation = messageProxy.updateConversationName(id, initiator, conversationName)
        Map model = [conversation: conversation, currentProfile: initiator]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([template: template] as JSON)
    }

    def runtimeExceptionThrown(RuntimeException exception) {
        return render(status: exception.message as Integer, text: 'An error occurred')
    }

    def accessDeniedExceptionThrown(AccessDeniedException exception) {
        log.error exception.message
        return render(status: 403, text: 'An authorization error occurred')
    }
}
