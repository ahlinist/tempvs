package club.tempvs.message

class UpdateParticipantsPayload {

    Participant initiator
    Participant subject
    Action action

    enum Action {
        ADD, REMOVE
    }
}
