package com.tempvs.tests.user

import com.tempvs.controllers.UserRegisterCommand
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {
    def userService
    Boolean userExistedBefore
    String email = 'test@mail.com'
    String password = 'passW0rd'

    def setup() {
        userExistedBefore = userService.checkIfUserExists(email)
        def urc = new UserRegisterCommand(email: email,
                                          password: password,
                                          repeatPassword: password,
                                          firstName: 'Test_first_name', lastName: 'Test_last_name')

        userService.createUser(urc.properties)
    }

    def cleanup() {
    }

    void "User with given email did not exist before running the test"() {
        expect: "User with given email did not exist before running the test"
        userExistedBefore == false
    }

    void "User with given email created"() {
        expect:"User with 'test@mail.com' email created"
            User.findByEmail(email) != null
    }

    void "User's password encrypted"() {
        expect:"User's password was encrypted"
            User.findByEmail(email).password != password
    }

    void "User with given password created"() {
        expect:"User with ${password} password created"
            User.findByEmail(email).password == userService.encrypt(password)
    }

    void "Created user has userProfile"() {
        expect:"Created user has userProfile"
            User.findByEmail(email).userProfile instanceof UserProfile
    }
}
