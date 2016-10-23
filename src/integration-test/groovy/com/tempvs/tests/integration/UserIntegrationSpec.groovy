package com.tempvs.tests.integration

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
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
    def mailService
    private static final String EMAIL = 'userIntegrationTest@mail.com'
    private static final String EMAIL_FOR_VERIFICATION = 'userIntegrationVerificationTest@mail.com'
    private static final String EMAIL_FOR_UPDATE = 'userIntegrationTestUpdated@mail.com'
    private static final String EMAIL_TO_FAIL = 'userIntegrationTestToFail@mail.com'
    private static final String PROFILE_EMAIL_TO_FAIL = 'userIntegrationTestProfileEmailToFail@mail.com'
    private static final String NON_UNIQUE_EMAIL = 'userIntegrationTestNonUnique@mail.com'
    private static final String NOT_EMAIL = 'not email'
    private static final String PASSWORD = 'passW0rd'
    private static final String NEW_PASSWORD = 'newPassW0rd'
    private static final String FIRST_NAME = 'Test_first_name'
    private static final String LAST_NAME = 'Test_last_name'
    private static final String CUSTOM_ID = 'test.Custom-Id_12'
    private static final String NUMERIC_CUSTOM_ID = '123456'
    private static final String REGISTER_USER_ACTION = 'registerUser'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'

    @Shared
    Map fieldMap = [firstName: 'TestFirstName2', lastName: 'TestLastName',
                    location: 'testLocation', customId: CUSTOM_ID]

    def setup() {
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME, lastName: LAST_NAME)
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

    void "User with given email created"() {
        given: "User retrieved from DB"
        User user = User.findByEmail(EMAIL)

        expect: "User with ${EMAIL} email exists in DB"
        user

        and: "Created user has userProfile"
        user.userProfile.firstName == FIRST_NAME
    }

    void "User with incorrect email is not created"() {
        when: "Create user with incorrect email"
        userService.createUser(email: NOT_EMAIL, password: PASSWORD, firstName: FIRST_NAME, lastName: LAST_NAME)

        then: "User with incorrect email is not created"
        !User.findByEmail(NOT_EMAIL)
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
        userService.createUser(email: EMAIL_TO_FAIL, password: PASSWORD, firstName: FIRST_NAME, lastName: LAST_NAME)

        when: "Try to set non-unique email"
        userService.updateEmail(EMAIL_TO_FAIL)

        then: "Non-unique email has not been persisted"
        User.findAllByEmail(EMAIL_TO_FAIL).size() == 1

        and: "Old unique value is used instead"
        User.findAllByEmail(EMAIL).size() == 1
    }

    void "Check if email update is rejected for non-unique email from other user's userProfile"() {
        given: "Register additional user and set profileEmail"
        userService.createUser(email: EMAIL_TO_FAIL, password: PASSWORD, firstName: FIRST_NAME, lastName: LAST_NAME)
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

    void "Attempt to set numeric customId"() {
        when: 'set numeric customId'
        userService.updateUserProfile([customId: NUMERIC_CUSTOM_ID])

        then: 'numeric customId is not saved, old value is used instead'
        User.findByEmail(EMAIL).userProfile.customId == CUSTOM_ID
    }

    void "Update user profile fields"() {
        when: "Assign values"
        userService.updateUserProfile(fieldMap)

        then: "Values are saved in DB"
        User.findByEmail(EMAIL).userProfile."${fieldName}" == fieldValue

        where:
        fieldName            | fieldValue
        'firstName'          | fieldMap.firstName
        'lastName'           | fieldMap.lastName
        'customId'           | fieldMap.customId
        'location'           | fieldMap.location
    }

    void "Attempt to set non-unique profileEmail"() {
        when: "Create additional user"
        userService.createUser(email: NON_UNIQUE_EMAIL, password: PASSWORD, firstName: FIRST_NAME)

        and: "Set additional user's login email as user's profileEmail"
        userService.updateUserProfile([profileEmail: NON_UNIQUE_EMAIL])

        then: "Check that non-unique profileEmail was not assigned"
        User.findByEmail(EMAIL).userProfile.profileEmail != NON_UNIQUE_EMAIL
    }

    void "create email verification entries"() {
        given:
        Map registerParams = [email: EMAIL_FOR_VERIFICATION,
                      destination: EMAIL_FOR_VERIFICATION,
                      action: REGISTER_USER_ACTION,
                      firstName: FIRST_NAME,
                      lastName: LAST_NAME,
                      password: PASSWORD]
        Map emailUpdateParams = [email: EMAIL_FOR_VERIFICATION,
                                 destination: EMAIL_FOR_VERIFICATION,
                                 action: UPDATE_EMAIL_ACTION]
        Map profileEmailUpdateParams = [email: EMAIL_FOR_VERIFICATION,
                                        destination: EMAIL_FOR_VERIFICATION,
                                        action: UPDATE_PROFILE_EMAIL_ACTION]
        mailService.metaClass.sendMail = {}

        when: 'create registration verification'
        userService.createEmailVerification(registerParams)

        then: 'find it in DB with generated verification code'
        EmailVerification.findByEmailAndAction(EMAIL_FOR_VERIFICATION, REGISTER_USER_ACTION).verificationCode

        when: 'create email update verification'
        userService.createEmailVerification(emailUpdateParams)

        then: 'find it in DB with generated verification code'
        EmailVerification.findByEmailAndAction(EMAIL_FOR_VERIFICATION, UPDATE_EMAIL_ACTION).verificationCode

        when: 'create profile email update verification'
        userService.createEmailVerification(profileEmailUpdateParams)

        then: 'find it in DB with generated verification code'
        EmailVerification.findByEmailAndAction(EMAIL_FOR_VERIFICATION, UPDATE_PROFILE_EMAIL_ACTION).verificationCode
    }
}
