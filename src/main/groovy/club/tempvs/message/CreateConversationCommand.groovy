package club.tempvs.message

import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class CreateConversationCommand implements Validateable {
    List<Profile> receivers
    String text
    String name

    static constraints = {
        receivers size: 1..19, nullable: false
        name nullable: true, blank: true
    }
}
