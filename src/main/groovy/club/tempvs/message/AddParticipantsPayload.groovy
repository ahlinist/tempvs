package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class AddParticipantsPayload {
    Participant initiator
    List<Participant> subjects
}
