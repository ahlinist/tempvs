package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class ReadMessagesPayload {
    Participant participant
    List messageIds
}
