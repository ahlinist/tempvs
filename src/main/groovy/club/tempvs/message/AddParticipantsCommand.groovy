package club.tempvs.message

import club.tempvs.user.Profile
import grails.validation.Validateable
import groovy.transform.CompileStatic

@CompileStatic
class AddParticipantsCommand implements Validateable {
    List<Profile> participants
}
