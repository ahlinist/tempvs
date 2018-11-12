package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class UpdateConversationNamePayload {
    String name
    Participant initiator
}
