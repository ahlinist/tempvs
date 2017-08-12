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

    def userService = Mock(UserService) {
        isEmailUnique(_ as String) >> Boolean.TRUE
    }

    RequestRegistrationCommand requestRegistrationCommand

    def setup() {
        requestRegistrationCommand = new RequestRegistrationCommand(userService: userService)
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

    void "RequestRegistrationCommand should contain unique email"() {
        given:
        def userService = Mock(UserService) {
            isEmailUnique(_ as String) >> Boolean.FALSE
        }

        and:
        requestRegistrationCommand.userService = userService
        requestRegistrationCommand.email = VALID_EMAIL

        expect:
        !requestRegistrationCommand.validate()
    }

    void "RequestRegistrationCommand should contain email that is not owned by one of the verifications"() {
        given:
        Map properties = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        new EmailVerification(properties).save(flush: true)
        requestRegistrationCommand.email = VALID_EMAIL

        expect:
        !requestRegistrationCommand.validate()
    }
}
