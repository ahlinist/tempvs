package club.tempvs.message

import club.tempvs.profile.ProfileDto
import groovy.transform.CompileStatic

@CompileStatic
class MessageBean {
    Long id
    String text
    ProfileDto author
    ProfileDto subject
    String createdDate
    Boolean unread
    Boolean system
}
