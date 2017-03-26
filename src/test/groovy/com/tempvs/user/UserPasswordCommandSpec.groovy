package com.tempvs.user

import com.tempvs.user.User
import com.tempvs.user.UserController
import com.tempvs.user.UserPasswordCommand
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification

@TestFor(UserController)
class UserPasswordCommandSpec extends Specification {
    private static final String CURRENT_PASSWORD = 'currentPassword'
    private static final String NEW_PASSWORD = 'newPassword'

    def springSecurityService = Mock(SpringSecurityService)
    def passwordEncoder = Mock(PasswordEncoder)

    def setup() {
        passwordEncoder.isPasswordValid(_, _, _) >> Boolean.TRUE
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

        expect:
        new UserPasswordCommand(props).validate()
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

        expect:
        !new UserPasswordCommand(props).validate()
    }
}
