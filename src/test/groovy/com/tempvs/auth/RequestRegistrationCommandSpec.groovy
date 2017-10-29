package com.tempvs.auth

import com.tempvs.user.*
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * Unit-test suite for {@link com.tempvs.auth.RequestRegistrationCommand} testing.
 */
@TestFor(AuthController)
@Mock([User, UserProfile, ClubProfile, EmailVerification])
class RequestRegistrationCommandSpec extends Specification {

    private static final INVALID_EMAIL = 'test-email.com'
    private static final VALID_EMAIL = 'test@email.com'
    private static final String REGISTER_ACTION = 'registration'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def verifyService = Mock(VerifyService) {
        isEmailUnique(_, _, _) >> Boolean.TRUE
    }

    RequestRegistrationCommand requestRegistrationCommand

    def setup() {
        requestRegistrationCommand = new RequestRegistrationCommand()
    }

    def cleanup() {
    }

    void "Empty RequestRegistrationCommand is invalid"() {
        expect:
        !requestRegistrationCommand.validate()
    }

    void "RequestRegistrationCommand should contain email"() {
        given:
        requestRegistrationCommand.email = VALID_EMAIL

        expect:
        requestRegistrationCommand.validate()
    }

    void "RequestRegistrationCommand should contain valid email"() {
        given:
        requestRegistrationCommand.email = INVALID_EMAIL

        expect:
        !requestRegistrationCommand.validate()
    }
}
