package com.tempvs.user

import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification

/**
 * A test-suite for command object handles a password value for {@link com.tempvs.user.User} entity.
 */
class UserPasswordCommandSpec extends Specification {

    private static final String PASSWORD = 'password'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String CURRENT_PASSWORD = 'currentPassword'

    def user = Mock User
    def userService = Mock UserService
    def passwordEncoder = Mock PasswordEncoder

    def setup() {
        userService.currentUser >> user
    }

    def cleanup() {

    }

    void "Create empty UserPasswordCommand" () {
        expect:
        !new UserPasswordCommand().validate()
    }

    void "Create full UserPasswordCommand" () {
        given:
        Map props = [
                currentPassword: CURRENT_PASSWORD,
                newPassword: NEW_PASSWORD,
                repeatNewPassword: NEW_PASSWORD,
                passwordEncoder: passwordEncoder,
                userService: userService,
        ]

        when:
        Boolean result = new UserPasswordCommand(props).validate()

        then:
        1 * userService.currentUserPassword >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, CURRENT_PASSWORD, null) >> Boolean.TRUE
        0 * _

        and:
        result
    }

    void "Check repeated pass matching" () {
        given:
        Map props = [
                currentPassword: CURRENT_PASSWORD,
                newPassword: NEW_PASSWORD,
                repeatNewPassword: CURRENT_PASSWORD,
                passwordEncoder: passwordEncoder,
                userService: userService,
        ]

        when:
        Boolean result = new UserPasswordCommand(props).validate()

        then:
        1 * userService.currentUserPassword >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, CURRENT_PASSWORD, null) >> Boolean.TRUE
        0 * _

        and:
        !result
    }
}
