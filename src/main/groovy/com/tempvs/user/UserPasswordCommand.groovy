package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import org.springframework.security.authentication.encoding.PasswordEncoder
/**
 * Command object for {@link com.tempvs.user.User}'s password update.
 */
@GrailsCompileStatic
class UserPasswordCommand implements Validateable {

    static PasswordEncoder passwordEncoder
    static SpringSecurityService springSecurityService

    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        currentPassword validator: { String password, UserPasswordCommand command ->
            User user = springSecurityService.currentUser as User
            passwordEncoder.isPasswordValid(user.password, password, null)
        }
        repeatNewPassword validator: { String password, UserPasswordCommand command ->
            command.newPassword == password
        }
    }
}
