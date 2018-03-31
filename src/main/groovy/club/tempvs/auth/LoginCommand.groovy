package club.tempvs.auth

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for {@link User} logging in.
 */
@GrailsCompileStatic
class LoginCommand implements Validateable {
    String email
    String password
    Boolean remember

    static constraints = {
        email email: true
        remember nullable: true
    }
}