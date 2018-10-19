package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class ConversationDto {
    Long id
    String type
    ParticipantDto admin
    Set<ParticipantDto> participants
    MessageDtoBean lastMessage
    List<MessageDtoBean> messages
}
