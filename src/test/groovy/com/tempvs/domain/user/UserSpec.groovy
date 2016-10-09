package com.tempvs.domain.user

import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {
    private static final String EMAIL = 'test@mail.com'
    private static final String INVALID_EMAIL = 'test-email.com'
    private static final String PASSWORD = 'passW0rd'

    def setup() {
    }

    def cleanup() {
    }

    void "non-valid user not created"() {
        when:"creating user wthout password"
            UnitTestUtils.createUser(EMAIL, null)

        and:"creating user without email"
            UnitTestUtils.createUser(null, PASSWORD)

        then:"user is not created"
            User.list().size() == 0
    }

    void "user created"() {
        when:"creating a user"
            UnitTestUtils.createUser(EMAIL)

        then:"DB contains user with given email"
            User.findByEmail(EMAIL)
    }

    void "users with only unique emails are saved"() {
        given:"created user"
            UnitTestUtils.createUser(EMAIL)

        when:"duplicating the existent user"
            UnitTestUtils.createUser(EMAIL)


        then:"only one user with given email"
            User.findAllByEmail(EMAIL).size() == 1
    }

    void "user with invalid email is not saved"() {
        when:"creating a user with non-valid email"
            UnitTestUtils.createUser(INVALID_EMAIL)


        then:"user with invalid email not saved"
            !User.findByEmail(INVALID_EMAIL)
    }
}
