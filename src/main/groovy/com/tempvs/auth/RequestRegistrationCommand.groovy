package com.tempvs.auth

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

    static constraints = {
        email email: true, blank: false
    }
}
