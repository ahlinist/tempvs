package com.tempvs.domain.user.verification

import com.tempvs.tests.utils.TestingUtils
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EmailVerification)
class EmailVerificationSpec extends Specification {
    private static final String ADDITIONAL_DESTINATION = 'additionalDestination@test.com'

    def setup() {
    }

    def cleanup() {
    }

    void "RegisterUser verification created"() {
        expect:'RegisterUser verification created'
        TestingUtils.createEmailVerification()
    }

    void "UpdateEmail verification created"() {
        expect:'UpdateEmail verification created'
        TestingUtils.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS)
    }

    void "UpdateProfileEmail verification created"() {
        expect:'UpdateProfileEmail verification created'
        TestingUtils.createEmailVerification(TestingUtils.DEFAULT_PROFILE_EMAIL_VERIFICATION_PROPS)
    }

    void "Only single entry is created for the same email and action"() {
        given: 'Create verification entry'
        TestingUtils.createEmailVerification()

        expect:'Duplicate is not created'
        !TestingUtils.createEmailVerification()
    }

    void "Only single entry is created for the same verification code"() {
        given: 'Create verification entry'
        Map props = TestingUtils.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS).properties
        props.destination = ADDITIONAL_DESTINATION
        props.userId = TestingUtils.USER_ID + 1
        props.action = TestingUtils.UPDATE_PROFILE_EMAIL_ACTION

        expect: 'VerificationCode duplicate is not created'
        !TestingUtils.createEmailVerification(props)
    }

    void "Verifications for different actions are created for the same mail"() {
        expect:"Create profile email update verification"
        TestingUtils.createEmailVerification()
        TestingUtils.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS)
        TestingUtils.createEmailVerification(TestingUtils.DEFAULT_PROFILE_EMAIL_VERIFICATION_PROPS)
    }
}
