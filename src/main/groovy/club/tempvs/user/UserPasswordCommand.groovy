package club.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Command object for {@link User}'s password update.
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
            command.passwordEncoder.matches(password, command.userService.currentUser?.password)
        }

        repeatNewPassword validator: { String repeatNewPassword, UserPasswordCommand command ->
            command.newPassword == repeatNewPassword
        }
    }
}
