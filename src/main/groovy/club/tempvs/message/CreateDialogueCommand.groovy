package club.tempvs.message

import club.tempvs.user.Profile
import grails.validation.Validateable
import groovy.transform.CompileStatic

@CompileStatic
class CreateDialogueCommand implements Validateable {
    Profile receiver
    String text
}
