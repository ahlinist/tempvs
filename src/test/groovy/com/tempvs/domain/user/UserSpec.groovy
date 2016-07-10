package com.tempvs.domain.user

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {
    String EMAIL = 'test@mail.com'
    String INVALID_EMAIL = 'test-email.com'
    String PASSWORD = 'passW0rd'

    def setup() {
        createUser(EMAIL, PASSWORD)
        createUser(EMAIL, PASSWORD)
        createUser(INVALID_EMAIL, PASSWORD)
    }

    def cleanup() {
    }

    void "DB contains user with given email"() {
        expect:"DB contains user with given email"
        User.findByEmail(EMAIL)
    }

    void "users with only unique emails are saved"() {
        expect:"only one user with given email"
        User.findAllByEmail(EMAIL).size() == 1
    }

    void "user with invalid email not saved"() {
        expect:"user with invalid email not saved"
        !User.findByEmail(INVALID_EMAIL)
    }

    private void createUser(String email, String password){
        User user = new User(email: email, password: password, lastActive: new Date())
        user.userProfile = new UserProfile(firstName:'firstName', lastName: 'lastName')
        user.save(flush:true)
    }
}
