package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
/**
 * Command object for {@link com.tempvs.user.User}'s registration.
 */
@GrailsCompileStatic
class RegisterCommand implements Validateable {
    String firstName
    String lastName
    String password
    String repeatPassword

    static constraints = {
        password blank: false, password: true
        repeatPassword validator: { String password, RegisterCommand command ->
            password == command.password
        }
    }
}