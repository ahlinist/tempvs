package com.tempvs.domain.user

import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
@Mock(User)
class UserProfileSpec extends Specification {
    private static final String EMAIL_FOR_FAIL = 'fail@mail.com'
    private static final String EMAIL_FOR_SUCCESS = 'success@mail.com'
    private static final String PASSWORD = 'passW0rd'
    private static final String FIRST_NAME = 'testFirstName'
    private static final String LAST_NAME = 'testLastName'
    private static final String CUSTOM_ID = 'testCustomId'
    private static final String PROFILE_EMAIL = 'profile@mail.com'
    private static final String LOCATION = 'testLocation'

    def setup() {
    }

    def cleanup() {
    }

    void "test fail on first name missing"() {
        when:"creating a user without firstname"
            UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, null)


        then:"userProfile creation failed"
            UserProfile.list().size() == 0
    }

    void "userProfile created"() {
        when:"creating valid user"
            UnitTestUtils.createUser(EMAIL_FOR_SUCCESS, PASSWORD, FIRST_NAME, LAST_NAME, CUSTOM_ID, PROFILE_EMAIL, LOCATION)

        then: "pass all values and userProfile is created"
            UserProfile.findByFirstName(FIRST_NAME)
    }
}
