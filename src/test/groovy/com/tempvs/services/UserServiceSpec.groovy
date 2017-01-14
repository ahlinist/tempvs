package com.tempvs.services

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import com.tempvs.tests.utils.TestingUtils
import com.tempvs.tests.utils.user.WithUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(UserService)
@Mock([User, UserProfile, EmailVerification])
class UserServiceSpec extends Specification implements WithUser {
    private static final String NEW_EMAIL = 'newEmail@mail.com'
    private static final String NEW_PROFILE_EMAIL = 'newProfileEmail@mail.com'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)
        GroovySpy(EmailVerification, global: true)
        GroovySpy(Avatar, global: true)

        service.mailService = [sendMail: { Closure c-> }]

        service.springSecurityService = [
                encodePassword: { arg ->
                    arg
                }
        ]
    }

    def cleanup() {
    }

    void "Check getUser() method for id"() {
        when: 'getUser() is called'
        service.getUser(TestingUtils.USER_ID as String)

        then: 'User is queried in the DB'
        1 * User.get(TestingUtils.USER_ID) >> Mock(User)
    }

    void "Check getUser() method for customId"() {
        when: 'getUser() is called'
        service.getUser(TestingUtils.CUSTOM_ID)

        then: 'User is queried in the DB'
        1 * UserProfile.findByCustomId(TestingUtils.CUSTOM_ID) >> Mock(User)
    }

    void "Check getUserByEmail()"() {
        when: 'getUserByEmail() is called'
        service.getUserByEmail(TestingUtils.EMAIL)

        then: 'User is queried in the DB'
        1 * User.findByEmail(TestingUtils.EMAIL) >> Mock(User)
    }

    void "Check getUserByProfileEmail()"() {
        when: 'getUserByProfileEmail() is called'
        service.getUserByProfileEmail(TestingUtils.PROFILE_EMAIL)

        then: 'User is queried in the DB'
        1 * UserProfile.findByProfileEmail(TestingUtils.PROFILE_EMAIL) >> Mock(User)
    }

    void "Check creation of email verification"() {
        when: 'Calling createEmailVerification()'
        def result = service.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS)

        then: 'Verification constructor invoked and code generated'
        1 * new EmailVerification(_)
        result.verificationCode
    }

    void "Check user creation"() {
        when: 'Invoking createUser()'
        def result = service.createUser(TestingUtils.DEFAULT_USER_PROPS)

        then: "Appropriate constructors are called"
        1 * new User(_)
        1 * new UserProfile(_)
        1 * new Avatar()

        and: 'User is returned'
        result instanceof User
    }

    void "Check email update"() {
        expect: "updateEmail() changes user's email"
        service.updateEmail(user.id, NEW_EMAIL).email == NEW_EMAIL

        and: 'User with updated email is found in the DB'
        User.findByEmail(NEW_EMAIL)
    }

    void "Check password update"() {
        setup: 'Setting up currentUser'
        service.springSecurityService.currentUser = getUser()

        expect: "updatePassword() changes user's password"
        service.updatePassword(NEW_PASSWORD).password == NEW_PASSWORD

        and: 'User with updated password is found in the DB'
        User.findByPassword(NEW_PASSWORD)
    }

    void "Check updateLastActive()"() {
        given: 'Initial settings'
        service.springSecurityService.currentUser = user
        Date lastActiveOld = user.lastActive

        when: 'Invoking updateLastActive()'
        service.updateLastActive()

        then: 'Old active date differs from new one'
        lastActiveOld != user.lastActive
    }

    void "Check profile email update"() {
        expect: "updateProfileEmail() changes user's email"
        service.updateProfileEmail(user.id, NEW_PROFILE_EMAIL).profileEmail == NEW_PROFILE_EMAIL

        and: 'userProfile with updated profileEmail is found in the DB'
        UserProfile.findByProfileEmail(NEW_PROFILE_EMAIL)
    }

    void "Check update of user's profile"() {
        setup: 'Setting up currentUser'
        service.springSecurityService.currentUser = user

        expect: "updateUserProfile() changes user's password"
        service.updateUserProfile([location: 'newLocation']).location == 'newLocation'

        and: 'userProfile with updated location is found in the DB'
        UserProfile.findByLocation('newLocation')
    }

    void "Check getVerification()"() {
        when: 'Invoking getVerification()'
        service.getVerification(VERIFICATION_CODE)

        then: "Appropriate constructors are called"
        1 * EmailVerification.findByVerificationCode(VERIFICATION_CODE) >> Mock(EmailVerification)
    }
}
