package com.tempvs.user

import com.tempvs.user.RegisterCommand
import com.tempvs.user.UserController
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
class RegisterCommandSpec extends Specification {
    private static final String PASSWORD = 'password'
    public static final String FIRST_NAME = 'firstName'
    public static final String LAST_NAME = 'lastName'

    def setup() {

    }

    def cleanup() {

    }

    void "Create empty RegisterCommand"() {
        expect:
        !new RegisterCommand().validate()
    }

    void "Create full RegisterCommand"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, password: PASSWORD, repeatPassword: PASSWORD]

        expect:
        new RegisterCommand(props).validate()
    }

    void "Check password matching"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, password: PASSWORD, repeatPassword: PASSWORD + 1]

        expect:
        !new RegisterCommand(props).validate()
    }

    void "Create RegisterCommand with missing lastName"() {
        given:
        Map props = [firstName: FIRST_NAME, password: PASSWORD, repeatPassword: PASSWORD]

        expect:
        !new RegisterCommand(props).validate()
    }
}
