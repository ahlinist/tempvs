package com.tempvs.tests.integration

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.test.mixin.Mock
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
@Mock([User, UserProfile, Avatar])
class UserIntegrationSpec extends Specification {
    def userService
    def springSecurityService
    def passwordEncoder
    String EMAIL = 'userIntegrationTest@mail.com'
    String EMAIL_FOR_UPDATE = 'userIntegrationTestUpdated@mail.com'
    String EMAIL_TO_FAIL = 'userIntegrationTestToFail@mail.com'
    String PROFILE_EMAIL_TO_FAIL = 'userIntegrationTestProfileEmailToFail@mail.com'
    String NOT_EMAIL = 'not email'
    String PASSWORD = 'passW0rd'
    String NEW_PASSWORD = 'newPassW0rd'
    String FIRST_NAME = 'Test_first_name'

    def setup() {
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME)
        springSecurityService.reauthenticate(EMAIL, PASSWORD)
    }

    def cleanup() {
        Avatar.list().each {
            it.delete()
        }

        UserProfile.list().each {
            it.delete()
        }

        User.list().each {
            it.delete()
        }
    }

    void "User with incorrect email is not created"() {
        when: "Create user with incorrect email"
        userService.createUser(email: NOT_EMAIL, password: PASSWORD, firstName: FIRST_NAME)

        then: "User with incorrect email is not created"
        !User.findByEmail(NOT_EMAIL)
    }

    void "User with given email created"() {
        expect: "User with ${EMAIL} email exists in DB"
        User.findByEmail(EMAIL)
    }

    void "User's password encrypted"() {
        expect: "User's password was encrypted"
        User.findByEmail(EMAIL).password != PASSWORD
    }

    void "User with given password created"() {
        expect: "User with given password created"
        passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, PASSWORD, null)
    }

    void "Update user email"(){
        when: "Update email"
        userService.updateEmail(EMAIL_FOR_UPDATE)

        then: "Find user with updated email in DB"
        User.findByEmail(EMAIL_FOR_UPDATE)

        and: "Find no user with old email"
        !User.findByEmail(EMAIL)
    }

    void "Check if email update is rejected for non-unique email"() {
        given: "Register additional user"
        userService.createUser(email: EMAIL_TO_FAIL, password: PASSWORD, firstName: FIRST_NAME)

        when: "Try to set non-unique email"
        userService.updateEmail(EMAIL_TO_FAIL)

        then: "Non-unique email has not been persisted"
        User.findAllByEmail(EMAIL_TO_FAIL).size() == 1

        and: "Old unique value is used instead"
        User.findAllByEmail(EMAIL).size() == 1
    }

    void "Check if email update is rejected for non-unique email from other user's userProfile"() {
        given: "Register additional user and set profileEmail"
        userService.createUser(email: EMAIL_TO_FAIL, password: PASSWORD, firstName: FIRST_NAME)
        User user = User.findByEmail(EMAIL_TO_FAIL)
        user.userProfile.profileEmail = PROFILE_EMAIL_TO_FAIL
        user.save()

        when: "Assign email from other user's userProfile"
        userService.updateEmail(PROFILE_EMAIL_TO_FAIL)

        then: "Non-unique email has not been persisted"
        !User.findByEmail(PROFILE_EMAIL_TO_FAIL)

        and: "Old unique value is used instead"
        User.findByEmail(EMAIL)
    }

    void "Change password"() {
        when: "Change password"
        userService.updatePassword(NEW_PASSWORD)

        then: "New password is valid"
        passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, NEW_PASSWORD, null)

        and: "Old password not valid"
        !passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, PASSWORD, null)

        and: "New password encrypted"
        User.findByEmail(EMAIL).password != PASSWORD
    }
}
