package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class MessageBean {
    Long id
    String text
    Participant author
    Participant subject
    String createdDate
    Boolean unread
    Boolean system
}
