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
    private static final String EMAIL_FOR_FAIL = 'test4fail@mail.com'
    private static final String INVALID_EMAIL = 'test-email.com'
    private static final String PASSWORD = 'passW0rd'
    private static final String FIRST_NAME = 'testFirstName'
    private static final String LAST_NAME = 'testLastName'
    private static final String PROFILE_EMAIL = 'profile@mail.com'


    def setup() {
        UnitTestUtils.createUser(EMAIL)
    }

    def cleanup() {
    }

    void "Non-valid user not created"() {
        expect: "User without password has not been created"
        !UnitTestUtils.createUser(EMAIL_FOR_FAIL, null)

        and: "Creating user without email"
        !UnitTestUtils.createUser(null, PASSWORD)
    }

    void "User created"() {
        expect:"DB contains user with given email"
        User.findByEmail(EMAIL)
    }

    void "Users with only unique emails are saved"() {
        when:"Duplicating the existent user"
        UnitTestUtils.createUser(EMAIL)

        then:"Only one user with given email"
        User.findAllByEmail(EMAIL).size() == 1

        when: 'Creating additional user for uniqueness in profile email check'
        UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, LAST_NAME, null, EMAIL_FOR_FAIL)

        then: 'Additional user will not be created'
        !UnitTestUtils.createUser(EMAIL_FOR_FAIL)
    }

    void "User with invalid email is not saved"() {
        when:"Creating a user with non-valid email"
        UnitTestUtils.createUser(INVALID_EMAIL)

        then:"User with invalid email not saved"
        !User.findByEmail(INVALID_EMAIL)
    }
}
