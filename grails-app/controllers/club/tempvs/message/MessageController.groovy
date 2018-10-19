package club.tempvs.message

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator

@GrailsCompileStatic
class MessageController {

    private static final String DISPLAY_COUNTER = 'displayCounter'
    private static final String SELF_SENT_MESSAGE = 'message.selfsent.error'
    private static final String APPEND_ACTION = 'appendElement'
    private static final String REPLACE_ACTION = 'replaceElement'

    static allowedMethods = [
            getNewConversationsCount: 'GET',
            loadConversations: 'GET',
            conversation: 'GET',
            createDialogue: 'POST',
    ]

    ProfileService profileService
    MessageProxy messageProxy
    AjaxResponseHelper ajaxResponseHelper
    LinkGenerator grailsLinkGenerator
    PageRenderer groovyPageRenderer

    def index() {

    }

    def getNewConversationsCount() {
        Profile currentProfile = profileService.currentProfile
        Integer count = messageProxy.getNewConversationsCount(currentProfile)
        render([action: DISPLAY_COUNTER, count: count] as JSON)
    }

    def loadConversations() {
        Profile currentProfile = profileService.currentProfile
        ConversationsDto conversationsDto = messageProxy.getConversations(currentProfile, 0, 20)
        Map model = [conversations: conversationsDto.conversations]
        String template = groovyPageRenderer.render(template: '/message/templates/conversations', model: model)
        render([action: APPEND_ACTION, template: template] as JSON)
    }

    def loadMessages(Long id) {
        Profile currentProfile = profileService.currentProfile
        ConversationDto conversationDto = messageProxy.getConversation(id, currentProfile, 0, 20)
        Map model = [conversation: conversationDto]
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

        ConversationDto conversationDto = messageProxy.createConversation(currentProfile, [receiver], command.text)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'message', action: 'conversation', id: conversationDto.id))
    }
}
