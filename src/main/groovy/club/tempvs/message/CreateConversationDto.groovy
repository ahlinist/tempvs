package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class CreateConversationDto {
    ParticipantDto author
    List<ParticipantDto> receivers
    String text
    String name
}
