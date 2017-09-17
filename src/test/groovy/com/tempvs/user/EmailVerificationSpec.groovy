package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */

@TestFor(EmailVerification)
class EmailVerificationSpec extends Specification {

    private static final Long USER_ID = 1L
    private static final String UPD_EMAIL_ACTION = 'email'
    private static final String REGISTER_ACTION = 'registration'
    private static final String INVALID_ACTION = 'invalidAction'
    private static final String VALID_EMAIL = 'verification@email.com'
    private static final String VERIFICATION_CODE = 'verificationCode'
    private static final String INVALID_EMAIL = 'verification-email.com'
    private static final String UPD_PROFILE_EMAIL_ACTION = 'userProfile'

    def setup() {
    }

    def cleanup() {
    }

    void "Verification with no properties fails"() {
        expect:
        !new EmailVerification().validate()
    }

    void "Verification with valid properties passes"() {
        given:
        Map properties = [
                verificationCode: VERIFICATION_CODE,
                action: REGISTER_ACTION,
                email: VALID_EMAIL,
        ]

        expect:
        new EmailVerification(properties).validate()
    }

    void "Verification for email update without instanceId fails"() {
        given:
        Map properties = [
                verificationCode: VERIFICATION_CODE,
                action: UPD_EMAIL_ACTION,
                email: VALID_EMAIL,
        ]

        expect:
        !new EmailVerification(properties).validate()
    }

    void "Verifications for different actions are created for the same mail"() {
        given:
        Map registrationProperties = [
                verificationCode: VERIFICATION_CODE,
                action: REGISTER_ACTION,
                email: VALID_EMAIL,
        ]

        Map emailUpdateProperties = [
                verificationCode: VERIFICATION_CODE + 1,
                action: UPD_EMAIL_ACTION,
                email: VALID_EMAIL,
                instanceId: USER_ID,
        ]

        Map profileEmailUpdateProperties = [
                verificationCode: VERIFICATION_CODE + 2,
                action: UPD_PROFILE_EMAIL_ACTION,
                email: VALID_EMAIL,
                instanceId: USER_ID,
        ]

        expect:
        new EmailVerification(registrationProperties).save(flush: true)
        new EmailVerification(emailUpdateProperties).save(flush: true)
        new EmailVerification(profileEmailUpdateProperties).save(flush: true)
    }

    void "Verifications for non-listed action not saved"() {
        given:
        Map properties = [
                verificationCode: VERIFICATION_CODE,
                action: INVALID_ACTION,
                email: VALID_EMAIL,
        ]

        expect:
        !new EmailVerification(properties).validate()
    }

    void "Verifications for invalid email not saved"() {
        given:
        Map properties = [
                verificationCode: VERIFICATION_CODE,
                action: REGISTER_ACTION,
                email: INVALID_EMAIL,
        ]

        expect:
        !new EmailVerification(properties).validate()
    }

    void "Verification code should be unique"() {
        given:
        Map properties1 = [
                verificationCode: VERIFICATION_CODE,
                action: REGISTER_ACTION,
                email: VALID_EMAIL,
        ]

        Map properties2 = [
                verificationCode: VERIFICATION_CODE,
                action: UPD_EMAIL_ACTION,
                email: 'suffix' + VALID_EMAIL,
        ]

        expect:
        new EmailVerification(properties1).save(flush:true)
        !new EmailVerification(properties2).save(flush:true)
    }

    void "Email should be unique within 1 action" () {
        given:
        Map properties1 = [
                verificationCode: VERIFICATION_CODE,
                action: REGISTER_ACTION,
                email: VALID_EMAIL,
        ]

        Map properties2 = [
                verificationCode: VERIFICATION_CODE + 1,
                action: REGISTER_ACTION,
                email: VALID_EMAIL,
        ]

        expect:
        new EmailVerification(properties1).save(flush:true)
        !new EmailVerification(properties2).save(flush:true)
    }
}
