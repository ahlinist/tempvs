package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class MessageDtoBean {
    Long id
    String text
    ParticipantDto author
    ParticipantDto subject
    String createdDate
    Boolean unread
    Boolean system
}
