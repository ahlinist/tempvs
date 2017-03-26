package com.tempvs.user

import com.tempvs.tests.utils.TestingUtils
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {
    private static final String INVALID_EMAIL = 'test-email.com'

    def setup() {
    }

    def cleanup() {
    }

    void "User without password has not been created"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.password = null

        expect:
        !TestingUtils.createUser(props)
    }

    void "User without email has not been created"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.email = null

        expect:
        !TestingUtils.createUser(props)
    }

    void "User created"() {
        expect:
        TestingUtils.createUser()
    }

    void "Users with only unique emails are saved"() {
        expect:
        TestingUtils.createUser()
        !TestingUtils.createUser()
    }

    void "Check uniqueness between email and profileEmail"() {
        given:
        Map props = TestingUtils.createUser().properties
        props.email = props.profileEmail
        props.customId = null
        props.profileEmail = null

        expect:
        !TestingUtils.createUser(props)
    }

    void "User with invalid email is not saved"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.email = INVALID_EMAIL

        expect:
        !TestingUtils.createUser(props)
    }
}
