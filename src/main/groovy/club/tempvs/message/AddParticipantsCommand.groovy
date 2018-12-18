package club.tempvs.message

import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class AddParticipantsCommand implements Validateable {
    List<Profile> participants
}
