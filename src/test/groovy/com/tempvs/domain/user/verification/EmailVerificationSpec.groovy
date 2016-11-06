package com.tempvs.domain.user.verification

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EmailVerification)
class EmailVerificationSpec extends Specification {
    private static final String EMAIL_1 = 'email1@test.com'
    private static final String EMAIL_2 = 'email2@test.com'
    private static final String EMAIL_3 = 'email3@test.com'
    private static final String PASSWORD = 'testPassword'
    private static final String REGISTER_ACTION = 'registerUser'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static final String FIRST_NAME = 'testFirstName'
    private static final String LAST_NAME = 'testLastName'
    private static final String VERIFICATION_CODE_1 = 'theFirstVerificationCode1'
    private static final String VERIFICATION_CODE_2 = 'theFirstVerificationCode2'
    private static final String VERIFICATION_CODE_3 = 'theFirstVerificationCode3'
    private static final String DESTINATION_1 = 'destination1@test.com'
    private static final String DESTINATION_2 = 'destination2@test.com'
    private static final String DESTINATION_3 = 'destination3@test.com'

    def setup() {
        createEmailVerification(null, VERIFICATION_CODE_1, DESTINATION_1, REGISTER_ACTION, EMAIL_1, PASSWORD, FIRST_NAME, LAST_NAME)
    }

    def cleanup() {
    }

    void "email verification created"() {
        expect:"email verification created"
        EmailVerification.findByVerificationCode(VERIFICATION_CODE_1)
    }

    void "only single entry is created for the same email and action"() {
        given: "create duplicate entry"
        createEmailVerification(null, VERIFICATION_CODE_2, DESTINATION_1, REGISTER_ACTION, EMAIL_1, PASSWORD, FIRST_NAME, LAST_NAME)

        expect:"email verification created"
        EmailVerification.findByVerificationCode(VERIFICATION_CODE_1)

        and:"duplicate entry creation skipped"
        !EmailVerification.findByVerificationCode(VERIFICATION_CODE_2)
    }

    void "only single entry is created for the same verification code"() {
        given: "create duplicate entry"
        createEmailVerification(null, VERIFICATION_CODE_1, DESTINATION_2, REGISTER_ACTION, EMAIL_2, PASSWORD, FIRST_NAME, LAST_NAME)

        expect:"no duplicated email verification created"
        !EmailVerification.findByDestination(DESTINATION_2)
    }

    void "email updates may not contain pwd and names" () {
        when: "create email and profile email update entry"
        createEmailVerification(1, VERIFICATION_CODE_2, DESTINATION_2, UPDATE_EMAIL_ACTION)
        createEmailVerification(1, VERIFICATION_CODE_3, DESTINATION_3, UPDATE_PROFILE_EMAIL_ACTION)

        then: "email update entry found"
        EmailVerification.findByDestination(DESTINATION_2)

        and: "profile email update entry found"
        EmailVerification.findByDestination(DESTINATION_3)
    }

    void "verifications for different actions are created for the same mail"() {
        when: "create entry for email update"
        createEmailVerification(2, VERIFICATION_CODE_2, DESTINATION_1, UPDATE_EMAIL_ACTION)
        createEmailVerification(2, VERIFICATION_CODE_3, DESTINATION_1, UPDATE_PROFILE_EMAIL_ACTION)

        then:"email verifications created"
        EmailVerification.findAllByDestination(DESTINATION_1).size() == 3
    }

    private createEmailVerification(Long userId, String verificationCode, String destination, String action, String email = null,
                                    String password = null, String firstName = null, String lastName = null) {
        new EmailVerification(email: email, password: password, firstName: firstName, lastName:
                lastName,  action: action, verificationCode: verificationCode, destination: destination, userId: userId).save(flush: true)
    }
}
