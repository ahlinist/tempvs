package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class Conversation {
    Long id
    String type
    Participant admin
    Set<Participant> participants
    MessageBean lastMessage
    List<MessageBean> messages
}
