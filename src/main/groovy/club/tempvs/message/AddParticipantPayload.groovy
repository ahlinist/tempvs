package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class AddParticipantPayload {

    Participant initiator
    Participant subject
}
