package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class CreateConversationPayload {
    Participant author
    List<Participant> receivers
    String text
    String name
}
