package com.tempvs.tests.integration

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import com.tempvs.tests.utils.TestingUtils
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

    private static final String UPDATED = 'updated'

    private static Map userProfileUpdateFields = [
            firstName: UPDATED + TestingUtils.FIRST_NAME,
            lastName:  UPDATED + TestingUtils.LAST_NAME,
            location:  UPDATED + TestingUtils.LOCATION,
            customId:  UPDATED + TestingUtils.CUSTOM_ID
    ]
    private static Map emailUpdateVerificationParams = [
            destination: TestingUtils.UPDATE_EMAIL_DESTINATION,
            action: TestingUtils.UPDATE_EMAIL_ACTION
    ]
    private static Map profileEmailUpdateVerificationParams = [
            destination: TestingUtils.UPDATE_PROFILE_EMAIL_DESTINATION,
            action: TestingUtils.UPDATE_PROFILE_EMAIL_ACTION
    ]

    def setup() {
        userService.createUser(TestingUtils.DEFAULT_USER_PROPS)
        springSecurityService.reauthenticate(TestingUtils.EMAIL, TestingUtils.PASSWORD)
    }

    def cleanup() {
    }

    void "Check user creation"() {
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)

        expect: "Valid user created"
        user.validate()

        and: "Password encrypted"
        user.password != TestingUtils.PASSWORD
        passwordEncoder.isPasswordValid(user.password, TestingUtils.PASSWORD, null)

        and: "User's profile contains proper first and last names"
        user.userProfile.firstName == TestingUtils.FIRST_NAME
        user.userProfile.lastName == TestingUtils.LAST_NAME
    }

    void "Check user retrieving functionality"() {
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)

        expect: 'User is retrieved by id'
        userService.getUser(user.id as String)

        and: 'User is retrieved by customId'
        userService.getUser(user.userProfile.customId)

        and: 'User is retrieved by email'
        userService.getUserByEmail(user.email)
    }

    void "Update user's email"(){
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)
        String updatedEmail = UPDATED + TestingUtils.EMAIL

        when: "Update email"
        userService.updateEmail(user.id, updatedEmail)

        then: "Find user with updated email in DB"
        User.findByEmail(updatedEmail)

        and: "Find no user with old email"
        !User.findByEmail(TestingUtils.EMAIL)
    }

    void "Change password"() {
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)
        String newPassword = UPDATED + TestingUtils.PASSWORD

        when: "Change password"
        userService.updatePassword(newPassword)

        then: "New password is encrypted"
        user.password != newPassword

        and: "New password is valid"
        passwordEncoder.isPasswordValid(user.password, newPassword, null)

        and: "Old password is not valid"
        !passwordEncoder.isPasswordValid(user.password, TestingUtils.PASSWORD, null)
    }

    void "Update profile email" () {
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)
        String oldProfileEmail = user.userProfile.profileEmail
        String updatedProfileEmail = UPDATED + TestingUtils.PROFILE_EMAIL

        when: 'Changing profile email'
        userService.updateProfileEmail(user.id, updatedProfileEmail)

        then: 'Profile email is persisted'
        user.userProfile.profileEmail == updatedProfileEmail

        and: 'No user with old profile email exists'
        !UserProfile.findByProfileEmail(oldProfileEmail)
    }

    void "Update user profile fields"() {
        given: 'Find created user in DB'
        User user = User.findByEmail(TestingUtils.EMAIL)

        when: "Assign values"
        userService.updateUserProfile(userProfileUpdateFields)

        then: "Values are saved in DB"
        user.userProfile."${fieldName}" == fieldValue

        where:
        fieldName            | fieldValue
        'firstName'          | userProfileUpdateFields.firstName
        'lastName'           | userProfileUpdateFields.lastName
        'customId'           | userProfileUpdateFields.customId
        'location'           | userProfileUpdateFields.location
    }

    void "Check email verifications"() {
        given: 'Turning off the email sending'
        userService.mailService.metaClass.sendMail = { Closure c-> }

        when: 'Create the corresponding notification'
        userService.createEmailVerification(TestingUtils.DEFAULT_REGISTER_VERIFICATION_PROPS)

        then: 'Email notification objects found in DB with the generated verificationCode'
        String code1 = EmailVerification.
                findByEmailAndAction(TestingUtils.DESTINATION, TestingUtils.REGISTER_USER_ACTION).verificationCode

        when: 'Create new user via email verification object conversion'
        Long userId = userService.createUser(EmailVerification.findByVerificationCode(code1).properties).id

        then: 'Find the user in DB'
        User.findByEmail(TestingUtils.DESTINATION)

        when: 'Creating email and profileEmail update notifications'
        userService.createEmailVerification(profileEmailUpdateVerificationParams + [userId: userId])
        userService.createEmailVerification(emailUpdateVerificationParams + [userId: userId])

        then: 'Checking if the verifications have been created'
        String code2 = EmailVerification.
                findByUserIdAndAction(userId, TestingUtils.UPDATE_PROFILE_EMAIL_ACTION).verificationCode
        String code3 = EmailVerification.
                findByUserIdAndAction(userId, TestingUtils.UPDATE_EMAIL_ACTION).verificationCode

        when: 'Change profile email'
        EmailVerification verification4profileEmail = EmailVerification.findByVerificationCode(code2)
        userService.updateProfileEmail(verification4profileEmail.userId, verification4profileEmail.destination)

        then: 'UserProfile has an updated email'
        User.findByEmail(TestingUtils.DESTINATION).userProfile.profileEmail == TestingUtils.UPDATE_PROFILE_EMAIL_DESTINATION

        when: 'Change email'
        EmailVerification verification4email = EmailVerification.findByVerificationCode(code3)
        userService.updateEmail(verification4email.userId, verification4email.destination)

        then: 'No user with old email found'
        !User.findByEmail(TestingUtils.DESTINATION)

        and: 'User with new email found'
        User.findByEmail(TestingUtils.UPDATE_EMAIL_DESTINATION)
    }
}
