package club.tempvs.message

import club.tempvs.profile.ProfileDto
import club.tempvs.user.Profile
import groovy.transform.CompileStatic

@CompileStatic
class ConversationWrapper {
    Conversation conversation
    ProfileDto currentProfile

    ConversationWrapper(Conversation conversation, Profile currentProfile) {
        this.conversation = conversation
        this.currentProfile = new ProfileDto(currentProfile)
    }
}
