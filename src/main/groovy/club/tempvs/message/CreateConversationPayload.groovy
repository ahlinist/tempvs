package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class CreateConversationPayload {
    ProfileDto author
    List<ProfileDto> receivers
    String text
    String name
}
