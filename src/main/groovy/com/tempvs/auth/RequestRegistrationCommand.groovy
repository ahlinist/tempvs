package com.tempvs.auth

import com.tempvs.user.EmailVerification
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for {@link com.tempvs.user.User} registration request.
 * {@link com.tempvs.user.EmailVerification} instance is created as a result
 * and a corresponding email message with registration details is sent.
 */
@GrailsCompileStatic
class RequestRegistrationCommand implements Validateable {

    String email
    UserService userService

    static constraints = {
        email email: true, blank: false, validator: { String email, RequestRegistrationCommand command ->
            if (email) {
                command.userService.isEmailUnique(email) && !EmailVerification.findByEmail(email)
            } else {
                Boolean.TRUE
            }
        }
    }
}
