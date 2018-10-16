package club.tempvs.message

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON

@GrailsCompileStatic
class MessageController {

    private static final String DISPLAY_COUNTER = 'displayCounter'

    ProfileService profileService
    MessageProxy messageProxy

    def index() {
        Profile currentProfile = profileService.currentProfile
        ConversationsDto conversationsDto = messageProxy.getConversations(currentProfile, 0, 20)
        [conversationsDto: conversationsDto]
    }

    def getNewConversationsCount() {
        Profile currentProfile = profileService.currentProfile
        Integer count = messageProxy.getNewConversationsCount(currentProfile)
        render([action: DISPLAY_COUNTER, count: count] as JSON)
    }
}
