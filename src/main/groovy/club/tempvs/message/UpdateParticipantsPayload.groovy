package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class UpdateParticipantsPayload {

    Participant initiator
    Participant subject
    Action action

    enum Action {
        ADD, REMOVE
    }
}
