package com.tempvs.auth

import spock.lang.Specification

class LoginCommandSpec extends Specification {
    private static final String PASSWORD = 'passWord'
    private static final INVALID_EMAIL = 'test-email.com'
    private static final VALID_EMAIL = 'test@email.com'

    def setup() {

    }

    def cleanup() {

    }

    void "LoginCommand should contain email and password"() {
        expect:
        !new LoginCommand().validate()
        new LoginCommand(email: VALID_EMAIL, password: PASSWORD).validate()
    }

    void "LoginCommand should contain email"() {
        expect:
        !new LoginCommand(password: PASSWORD).validate()
    }

    void "LoginCommand should contain password"() {
        expect:
        !new LoginCommand(email: VALID_EMAIL).validate()
    }

    void "LoginCommand should contain valid email"() {
        expect:
        !new LoginCommand(email: INVALID_EMAIL, password: PASSWORD).validate()
    }
}
