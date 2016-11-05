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
    private static final String NOT_EMAIL = 'not email'
    private static final String EMAIL = 'success@mail.com'
    private static final String PASSWORD = 'passW0rd'
    private static final String FIRST_NAME = 'testFirstName'
    private static final String LAST_NAME = 'testLastName'
    private static final String CUSTOM_ID = 'testCustomId'
    private static final String NUMERIC_CUSTOM_ID = '123456'
    private static final String PROFILE_EMAIL = 'profile@mail.com'
    private static final String LOCATION = 'testLocation'

    def setup() {
        UnitTestUtils.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, CUSTOM_ID, PROFILE_EMAIL, LOCATION)
    }

    def cleanup() {
    }

    void "Test fail on one of the names missing"() {
        when:"Creating a user without lastName"
        UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, null)


        then:"UserProfile creation failed"
        !User.findByEmail(EMAIL_FOR_FAIL)

        when:"Creating a user without firstName"
        UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, null, LAST_NAME)


        then:"UserProfile creation failed"
        !User.findByEmail(EMAIL_FOR_FAIL)
    }

    void "User with incorrect email is not created"() {
        when: "Create user with incorrect email"
        UnitTestUtils.createUser(NOT_EMAIL, PASSWORD, FIRST_NAME, LAST_NAME)

        then: "User with incorrect email is not created"
        !User.findByEmail(NOT_EMAIL)
    }

    void "UserProfile created"() {
        expect: "Pass all values and userProfile is created"
        UserProfile.findByFirstName(FIRST_NAME)
    }

    void "User can not be created with numeric customId"() {
        when:"Creating invalid user"
        UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, LAST_NAME, NUMERIC_CUSTOM_ID)

        then:"UserProfile creation failed"
        !User.findByEmail(EMAIL_FOR_FAIL)

        and:
        !UserProfile.findByCustomId(NUMERIC_CUSTOM_ID)
    }

    void "Numeric customId can't be set to existent user"() {
        when: "Set numeric customId to existent user's profile"
        UserProfile userProfile = UserProfile.findByProfileEmail(PROFILE_EMAIL)
        userProfile.customId = NUMERIC_CUSTOM_ID
        userProfile.save(flush:true)

        then: "UserProfile has not been saved"
        !UserProfile.findByCustomId(NUMERIC_CUSTOM_ID)
    }

    void "Check if email update is rejected for non-unique email"() {
        given: "Register additional user with the same profile email"
        UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, LAST_NAME, CUSTOM_ID, PROFILE_EMAIL, LOCATION)

        expect: "Old unique value is used instead"
        !User.findByEmail(EMAIL_FOR_FAIL)
    }

    void "Try to set non-unique profile email"() {
        given: "Register additional user "
        UserProfile userProfile = UnitTestUtils.createUser(EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, LAST_NAME).userProfile

        when: "Set non-unique email"
        userProfile.profileEmail = PROFILE_EMAIL
        userProfile.save(flush: true)

        then: "Non-unique email has not been set"
        !UserProfile.findAllByProfileEmail(PROFILE_EMAIL).find {it.user.email == EMAIL_FOR_FAIL}
    }
}
