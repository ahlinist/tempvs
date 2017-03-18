package com.tempvs.domain.user.verification

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */

@TestFor(EmailVerification)
class EmailVerificationSpec extends Specification {
    private static final String REGISTER_ACTION = 'register'
    private static final String UPD_EMAIL_ACTION = 'updateEmail'
    private static final String UPD_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static final String INVALID_ACTION = 'invalidAction'
    private static final String VALID_EMAIL = 'verification@email.com'
    private static final String INVALID_EMAIL = 'verification-email.com'
    private static final String VERIFICATION_CODE = 'verificationCode'
    private static final Long USER_ID = 1L

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
        Map properties = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        expect:
        new EmailVerification(properties).validate()
    }

    void "Verification for email update without userId fails"() {
        given:
        Map properties = [verificationCode: VERIFICATION_CODE, action: UPD_EMAIL_ACTION, email: VALID_EMAIL]
        expect:
        !new EmailVerification(properties).validate()
    }

    void "Verifications for different actions are created for the same mail"() {
        given:
        Map regProps = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        Map mailUpdProps = [verificationCode: VERIFICATION_CODE + 1, action: UPD_EMAIL_ACTION,
                            email: VALID_EMAIL, userId: USER_ID]
        Map prfMailUpdProps = [verificationCode: VERIFICATION_CODE + 2, action: UPD_PROFILE_EMAIL_ACTION,
                               email: VALID_EMAIL, userId: USER_ID]

        expect:
        new EmailVerification(regProps).save(flush:true)
        new EmailVerification(mailUpdProps).save(flush:true)
        new EmailVerification(prfMailUpdProps).save(flush:true)
    }

    void "Verifications for non-listed action not saved"() {
        given:
        Map properties = [verificationCode: VERIFICATION_CODE, action: INVALID_ACTION, email: VALID_EMAIL]

        expect:
        !new EmailVerification(properties).save(flush:true)
    }

    void "Verifications for invalid email not saved"() {
        given:
        Map properties = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: INVALID_EMAIL]

        expect:
        !new EmailVerification(properties).save(flush:true)
    }

    void "Verification code should be unique"() {
        given:
        Map props1 = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        Map props2 = [verificationCode: VERIFICATION_CODE, action: UPD_EMAIL_ACTION,
                            email: 'suffix' + VALID_EMAIL]

        expect:
        new EmailVerification(props1).save(flush:true)
        !new EmailVerification(props2).save(flush:true)
    }

    void "Email should be unique within 1 action" () {
        given:
        Map props1 = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        Map props2 = [verificationCode: VERIFICATION_CODE + 1, action: REGISTER_ACTION, email: VALID_EMAIL]

        expect:
        new EmailVerification(props1).save(flush:true)
        !new EmailVerification(props2).save(flush:true)
    }
}
