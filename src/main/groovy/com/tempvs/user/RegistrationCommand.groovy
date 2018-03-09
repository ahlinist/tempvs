package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * Command object used for {@link com.tempvs.user.User} registration.
 */
@GrailsCompileStatic
class RegistrationCommand implements Validateable {

    String firstName
    String lastName
    String profileId
    String password
    String confirmPassword
    EmailVerification emailVerification

    static constraints = {
        confirmPassword validator: { String password, RegistrationCommand command ->
            password == command.password
        }

        password blank: false, password: true, size: 0..35

        firstName size: 0..35
        lastName size: 0..35
        profileId matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/, nullable: true
    }
}
