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
class UserSpec extends Specification {
    def userService
    def springSecurityService
    def passwordEncoder
    def mailService
    private static final String EMAIL = 'userIntegrationTest@mail.com'
    private static final String EMAIL_FOR_VERIFICATION = 'userIntegrationVerificationTest@mail.com'
    private static final String EMAIL_FOR_VERIFICATION_2 = 'userIntegrationVerificationTest2@mail.com'
    private static final String EMAIL_FOR_VERIFICATION_3 = 'userIntegrationVerificationTest2@mail.com'
    private static final String EMAIL_FOR_UPDATE = 'userIntegrationTestUpdated@mail.com'
    private static final String PROFILE_EMAIL = 'userIntegrationTestProfileEmail@mail.com'
    private static final String PASSWORD = 'passW0rd'
    private static final String NEW_PASSWORD = 'newPassW0rd'
    private static final String FIRST_NAME = 'Test_first_name'
    private static final String LAST_NAME = 'Test_last_name'
    private static final String CUSTOM_ID = 'test.Custom-Id_12'
    private static final String REGISTER_USER_ACTION = 'registerUser'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static Map fieldMap = [firstName: 'TestFirstName2', lastName: 'TestLastName',
                    location: 'testLocation', customId: CUSTOM_ID]

    def setup() {
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME, lastName: LAST_NAME)
        springSecurityService.reauthenticate(EMAIL, PASSWORD)
    }

    def cleanup() {
    }

    void "User with given email created"() {
        expect: "User with ${EMAIL} email exists in DB and has firstname in profile"
        User.findByEmail(EMAIL).userProfile.firstName == FIRST_NAME
    }

    void "User's password encrypted"() {
        expect: "User exists"
        User.findByEmail(EMAIL)

        and: "Password is not equal the initial one"
        User.findByEmail(EMAIL).password != PASSWORD

        and: "Password is encrypted"
        passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, PASSWORD, null)
    }

    void "Update user email"(){
        when: "Update email"
        userService.updateEmail(User.findByEmail(EMAIL).id, EMAIL_FOR_UPDATE)

        then: "Find user with updated email in DB"
        User.findByEmail(EMAIL_FOR_UPDATE)

        and: "Find no user with old email"
        !User.findByEmail(EMAIL)
    }

    void "Update profile email" () {
        when: 'Changing profile email'
        userService.updateProfileEmail(User.findByEmail(EMAIL).id, PROFILE_EMAIL)

        then: 'Profile email is persisted'
        User.findByEmail(EMAIL).userProfile.profileEmail == PROFILE_EMAIL
    }

    void "Change password"() {
        when: "Change password"
        userService.updatePassword(NEW_PASSWORD)

        then: "New password is encrypted"
        User.findByEmail(EMAIL).password != NEW_PASSWORD

        and: "New password is valid"
        passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, NEW_PASSWORD, null)

        and: "Old password is not valid"
        !passwordEncoder.isPasswordValid(User.findByEmail(EMAIL)?.password, PASSWORD, null)
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

    void "Create email verification entries"() {
        given: 'Initializing verification params'
        Map registerParams = [email: EMAIL_FOR_VERIFICATION, destination: EMAIL_FOR_VERIFICATION, action: REGISTER_USER_ACTION,
                              firstName: FIRST_NAME, lastName: LAST_NAME, password: PASSWORD]
        Map profileEmailUpdateParams = [destination: EMAIL_FOR_VERIFICATION_2, action: UPDATE_PROFILE_EMAIL_ACTION]
        Map emailUpdateParams = [destination: EMAIL_FOR_VERIFICATION_3, action: UPDATE_EMAIL_ACTION]

        and: 'Turning off the email sending'
        userService.mailService.metaClass.sendMail = { Closure c-> return true}

        when: 'Create the corresponding notification'
        userService.createEmailVerification(registerParams)

        then: 'Email notification objects found in DB with the generated verificationCode'
        String code1 = EmailVerification.findByEmailAndAction(EMAIL_FOR_VERIFICATION, REGISTER_USER_ACTION).verificationCode

        when: 'Create new user via email verification object conversion'
        Long userId = userService.createUser(EmailVerification.findByVerificationCode(code1).properties).id

        then: 'Find the user in DB'
        User.findByEmail(EMAIL_FOR_VERIFICATION)

        when: 'Creating email and profileEmail update notifications'
        userService.createEmailVerification(profileEmailUpdateParams + [userId: userId])
        userService.createEmailVerification(emailUpdateParams + [userId: userId])

        and: 'Picking the verification codes'
        String code2 = EmailVerification.findByUserIdAndAction(userId, UPDATE_PROFILE_EMAIL_ACTION).verificationCode
        String code3 = EmailVerification.findByUserIdAndAction(userId, UPDATE_EMAIL_ACTION).verificationCode

        then: 'Checking if the verifications have been created'
        code2
        code3

        when: 'Change profile email'
        EmailVerification verification4profileEmail = EmailVerification.findByVerificationCode(code2)
        userService.updateProfileEmail(verification4profileEmail.userId, verification4profileEmail.destination)

        then: 'UserProfile has an updated email'
        User.findByEmail(EMAIL_FOR_VERIFICATION).userProfile.profileEmail == EMAIL_FOR_VERIFICATION_2

        when: 'Change email'
        EmailVerification verification4email = EmailVerification.findByVerificationCode(code3)
        userService.updateEmail(verification4email.userId, verification4email.destination)

        then: 'No user with old email found'
        !User.findByEmail(EMAIL_FOR_VERIFICATION)

        and: 'User with new email found'
        User.findByEmail(EMAIL_FOR_VERIFICATION_3)
    }
}
