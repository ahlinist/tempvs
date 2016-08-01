package com.tempvs.tests.integration

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {
    def userService
    def passwordEncoder
    String EMAIL = 'test@mail.com'
    String NOT_EMAIL = 'not email'
    String PASSWORD = 'passW0rd'
    String FIRST_NAME = 'Test_first_name'

    def setup() {
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME)
        userService.createUser(email: NOT_EMAIL, password: PASSWORD, firstName: FIRST_NAME)
    }

    def cleanup() {
    }

    void "User with incorrect email is not created"() {
        expect: "User with incorrect email is not created"
        !User.findByEmail(NOT_EMAIL)
    }

    void "User with given email created"() {
        expect: "User with 'test@mail.com' email created"
        User.findByEmail(EMAIL)
    }

    void "User's password encrypted"() {
        expect: "User's password was encrypted"
        User.findByEmail(EMAIL).password != PASSWORD
    }

    void "User with given password created"() {
        expect: "User with given password created"
        passwordEncoder.isPasswordValid(User.findByEmail(EMAIL).password, PASSWORD, null)
    }

    void "Created user has userProfile"() {
        expect: "Created user has userProfile"
        User.findByEmail(EMAIL).userProfile
    }
}