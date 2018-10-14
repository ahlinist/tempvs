package club.tempvs.message

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class MessageController {

    ProfileService profileService
    MessageProxy messageProxy

    def index() {
        Profile profile = profileService.currentProfile
        ConversationsDto conversationsDto = messageProxy.getConversations(profile, 0, 20)
        [conversationsDto: conversationsDto]
    }
}
