package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification

@TestFor(UserController)
class UserPasswordCommandSpec extends Specification {
    private static final String CURRENT_PASSWORD = 'currentPassword'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String PASSWORD = 'password'

    def springSecurityService = Mock(SpringSecurityService)
    def passwordEncoder = Mock(PasswordEncoder)
    def user = Mock(User)

    def setup() {
        springSecurityService.currentUser >> Mock(User)
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
                springSecurityService: springSecurityService,
        ]

        when:
        Boolean result = new UserPasswordCommand(props).validate()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * user.password >> PASSWORD
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
                springSecurityService: springSecurityService,
        ]

        when:
        Boolean result = new UserPasswordCommand(props).validate()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * user.password >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, CURRENT_PASSWORD, null) >> Boolean.TRUE
        0 * _

        and:
        !result
    }
}
