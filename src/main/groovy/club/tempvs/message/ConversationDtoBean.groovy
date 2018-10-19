package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class ConversationDtoBean {
    Long id
    String type
    String name
    MessageDtoBean lastMessage
    String conversant
}
