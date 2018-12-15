package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class Conversation {
    Long id
    String type
    String name
    ProfileDto admin
    Set<ProfileDto> participants
    MessageBean lastMessage
    List<MessageBean> messages
}
