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

    def verifyService = Mock(VerifyService) {
        isEmailUnique(_ as String, _ as String, _ as Long) >> Boolean.TRUE
    }

    def emailVerification

    def setup() {
        emailVerification = new EmailVerification()
        emailVerification.verifyService = verifyService
    }

    def cleanup() {
    }

    void "Verification with no properties fails"() {
        expect:
        !emailVerification.validate()
    }

    void "Verification with valid properties passes"() {
        given:
        emailVerification.verificationCode = VERIFICATION_CODE
        emailVerification.action = REGISTER_ACTION
        emailVerification.email = VALID_EMAIL

        expect:
        emailVerification.validate()
    }

    void "Verification for email update without instanceId fails"() {
        given:
        emailVerification.verificationCode = VERIFICATION_CODE
        emailVerification.action = UPD_EMAIL_ACTION
        emailVerification.email = VALID_EMAIL

        expect:
        !emailVerification.validate()
    }

    void "Verifications for different actions are created for the same mail"() {
        given:
        EmailVerification regVerification = new EmailVerification()
        regVerification.verifyService = verifyService
        regVerification.verificationCode = VERIFICATION_CODE
        regVerification.action = REGISTER_ACTION
        regVerification.email = VALID_EMAIL

        and:
        EmailVerification emailVerification = new EmailVerification()
        emailVerification.verifyService = verifyService
        emailVerification.verificationCode = VERIFICATION_CODE + 1
        emailVerification.action = UPD_EMAIL_ACTION
        emailVerification.email = VALID_EMAIL
        emailVerification.instanceId = USER_ID

        and:
        EmailVerification profileVerification = new EmailVerification()
        profileVerification.verifyService = verifyService
        profileVerification.verificationCode = VERIFICATION_CODE + 2
        profileVerification.action = UPD_PROFILE_EMAIL_ACTION
        profileVerification.email = VALID_EMAIL
        profileVerification.instanceId = USER_ID

        expect:
        regVerification.save(flush: true)
        emailVerification.save(flush: true)
        profileVerification.save(flush: true)
    }

    void "Verifications for non-listed action not saved"() {
        given:
        emailVerification.verificationCode = VERIFICATION_CODE
        emailVerification.action = INVALID_ACTION
        emailVerification.email = VALID_EMAIL

        expect:
        !emailVerification.validate()
    }

    void "Verifications for invalid email not saved"() {
        given:
        emailVerification.verificationCode = VERIFICATION_CODE
        emailVerification.action = REGISTER_ACTION
        emailVerification.email = INVALID_EMAIL

        expect:
        !emailVerification.validate()
    }

    void "Verification code should be unique"() {
        given:
        EmailVerification emailVerification1 = new EmailVerification()
        emailVerification1.verifyService = verifyService
        emailVerification1.verificationCode = VERIFICATION_CODE
        emailVerification1.action = REGISTER_ACTION
        emailVerification1.email = VALID_EMAIL

        and:
        EmailVerification emailVerification2 = new EmailVerification()
        emailVerification2.verifyService = verifyService
        emailVerification2.verificationCode = VERIFICATION_CODE
        emailVerification2.action = UPD_EMAIL_ACTION
        emailVerification2.email = 'suffix' + VALID_EMAIL
        emailVerification2.instanceId = USER_ID

        expect:
        emailVerification1.save(flush:true)
        !emailVerification2.save(flush:true)
    }

    void "Email should be unique within 1 action" () {
        given:
        EmailVerification emailVerification1 = new EmailVerification()
        emailVerification1.verifyService = verifyService
        emailVerification1.verificationCode = VERIFICATION_CODE
        emailVerification1.action = REGISTER_ACTION
        emailVerification1.email = VALID_EMAIL

        and:
        EmailVerification emailVerification2 = new EmailVerification()
        emailVerification2.verifyService = verifyService
        emailVerification2.verificationCode = VERIFICATION_CODE + 1
        emailVerification2.action = REGISTER_ACTION
        emailVerification2.email = VALID_EMAIL

        expect:
        emailVerification1.save(flush:true)
        !emailVerification2.save(flush:true)
    }
}
