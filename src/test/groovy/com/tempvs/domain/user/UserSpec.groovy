package com.tempvs.domain.user

import com.tempvs.controllers.UserRegisterCommand
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {
    String email = 'test@mail.com'
    String password = 'passW0rd'

    def setup() {
        def urc = new UserRegisterCommand(email: email,
                password: password,
                repeatPassword: password,
                firstName: 'Test_first_name', lastName: 'Test_last_name')
        new User(urc.properties).save()
    }

    def cleanup() {
    }

    void "DB contains user with given email"() {
        expect:"DB contains user with given email"
            User.findByEmail(email).email == 'email'
    }
}
