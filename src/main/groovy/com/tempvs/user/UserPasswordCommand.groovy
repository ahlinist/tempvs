package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import org.springframework.security.authentication.encoding.PasswordEncoder

/**
 * Command object for {@link com.tempvs.user.User}'s password update.
 */
@GrailsCompileStatic
class UserPasswordCommand implements Validateable {

    PasswordEncoder passwordEncoder
    UserService userService

    String currentPassword
    String newPassword
    String repeatNewPassword

    static constraints = {
        newPassword size: 0..35

        currentPassword validator: { String password, UserPasswordCommand command ->
            command.passwordEncoder.isPasswordValid(command.userService.currentUserPassword, password, null)
        }

        repeatNewPassword validator: { String repeatNewPassword, UserPasswordCommand command ->
            command.newPassword == repeatNewPassword
        }
    }
}
