package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class AddMessagePayload {
    ProfileDto author
    String text
}
