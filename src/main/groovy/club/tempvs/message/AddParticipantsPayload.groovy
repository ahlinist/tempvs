package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class AddParticipantsPayload {
    ProfileDto initiator
    List<ProfileDto> subjects
}
