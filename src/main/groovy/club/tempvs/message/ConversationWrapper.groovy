package club.tempvs.message

import club.tempvs.user.Profile
import groovy.transform.CompileStatic

@CompileStatic
class ConversationWrapper {
    Conversation conversation
    Participant currentProfile

    ConversationWrapper(Conversation conversation, Profile currentProfile) {
        this.conversation = conversation
        this.currentProfile = new Participant(id: currentProfile.id, name: currentProfile.toString())
    }
}
