package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class ReadMessagesPayload {
    ProfileDto participant
    List messageIds
}
