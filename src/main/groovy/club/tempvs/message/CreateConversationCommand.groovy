package club.tempvs.message

import club.tempvs.user.Profile
import grails.validation.Validateable
import groovy.transform.CompileStatic

@CompileStatic
class CreateConversationCommand implements Validateable {
    List<Profile> receivers
    String text
    String name
}
