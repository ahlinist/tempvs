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

@Slf4j
@GrailsCompileStatic
class MessageController {

    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final String SELF_SENT_MESSAGE = 'message.selfsent.error'
    private static final String APPEND_ACTION = 'appendElement'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final Integer DEFAULT_PAGE_NUMBER = 0
    private static final Integer DEFAULT_PAGE_SIZE = 40

    static allowedMethods = [
            getNewConversationsCount: 'GET',
            loadConversations: 'GET',
            conversation: 'GET',
            createDialogue: 'POST',
            add: 'POST',
            updateParticipants: 'POST'
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
        ConversationsPayload conversationsPayload = messageProxy.getConversations(currentProfile, page, size)
        Map model = [conversations: conversationsPayload.conversations]
        String template = groovyPageRenderer.render(template: '/message/templates/conversations', model: model)
        render([action: APPEND_ACTION, template: template] as JSON)
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

    def createDialogue(CreateDialogueCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        Profile currentProfile = profileService.currentProfile
        Profile receiver = command.receiver

        if (currentProfile == receiver) {
            return render(ajaxResponseHelper.renderFormMessage(false, SELF_SENT_MESSAGE))
        }

        Conversation conversation = messageProxy.createConversation(currentProfile, [receiver], command.text)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'message', action: 'conversation', id: conversation.id))
    }

    def add(Long id) {
        Profile currentProfile = profileService.currentProfile
        Conversation conversation = messageProxy.addMessage(id, currentProfile, params.message as String)
        Map model = [conversation: conversation, currentProfile: currentProfile]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def updateParticipants(Long conversationId) {
        String actionString = params.updateAction as String
        Profile initiator = profileService.getProfileById(params.initiator as Long)
        Profile subject = profileService.getProfileById(params.subject as Long)
        UpdateParticipantsPayload.Action action = UpdateParticipantsPayload.Action.valueOf(actionString.toUpperCase())
        Conversation conversation = messageProxy.updateParticipants(conversationId, initiator, subject, action)
        Map model = [conversation: conversation, currentProfile: profileService.currentProfile]
        String template = groovyPageRenderer.render(template: '/message/templates/messages', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        log.error exception.message

        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }

    def exceptionThrown(Exception exception) {
        String refererLink = request.getHeader('referer')
        log.error exception.message

        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(refererLink)
        } else {
            redirect(refererLink)
        }
    }
}
