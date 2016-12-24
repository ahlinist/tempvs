package com.tempvs.domain.user

import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
@Mock([UserProfile, UserRole])
class UserSpec extends Specification {
    private static final String INVALID_EMAIL = 'test-email.com'

    def setup() {
    }

    def cleanup() {
    }

    void "User without password has not been created"() {
        given: 'Prepare properties'
        Map props = UnitTestUtils.DEFAULT_USER_PROPS.clone()
        props.password = null

        expect: "User without password has not been created"
        !UnitTestUtils.createUser(props)
    }

    void "User without email has not been created"() {
        given: 'Prepare properties'
        Map props = UnitTestUtils.DEFAULT_USER_PROPS.clone()
        props.email = null

        expect: "User without email has not been created"
        !UnitTestUtils.createUser(props)
    }

    void "User created"() {
        expect:"User created"
        UnitTestUtils.createUser(UnitTestUtils.DEFAULT_USER_PROPS)
    }

    void "Users with only unique emails are saved"() {
        given: 'Creating user'
        def email = UnitTestUtils.createUser().email

        expect:"Duplicating the existent user"
        !UnitTestUtils.createUser()

        and:"Only one user with given email exists"
        User.findAllByEmail(email).size() == 1
    }

    void "Check uniqueness between email and profileEmail"() {
        given: 'Creating user'
        Map props = UnitTestUtils.createUser().properties
        props.email = props.profileEmail
        props.customId = null
        props.profileEmail = null

        expect: "User with profileEmail equal to other user\'s email has not been created"
        !UnitTestUtils.createUser(props)
    }

    void "User with invalid email is not saved"() {
        given: 'Prepare properties'
        Map props = UnitTestUtils.DEFAULT_USER_PROPS.clone()
        props.email = INVALID_EMAIL

        expect:"User with non-valid email is not created"
        !UnitTestUtils.createUser(props)
    }
}
