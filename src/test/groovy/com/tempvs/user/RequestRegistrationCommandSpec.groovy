package com.tempvs.user

import com.tempvs.tests.utils.TestingUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AuthController)
@Mock([User, UserProfile, EmailVerification])
class RequestRegistrationCommandSpec extends Specification {
    private static final INVALID_EMAIL = 'test-email.com'
    private static final VALID_EMAIL = 'test@email.com'
    private static final String REGISTER_ACTION = 'register'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def setup() {

    }

    def cleanup() {

    }

    void "RequestRegistrationCommand should contain email"() {
        expect:
        !new RequestRegistrationCommand().validate()
        new RequestRegistrationCommand(email: VALID_EMAIL).validate()
    }

    void "RequestRegistrationCommand should contain valid email"() {
        expect:
        !new RequestRegistrationCommand(email: INVALID_EMAIL).validate()
    }

    void "RequestRegistrationCommand should contain email that is not owned by one of the users"() {
        given:
        TestingUtils.createUser()

        expect:
        !new RequestRegistrationCommand(email: TestingUtils.EMAIL).validate()
    }

    void "RequestRegistrationCommand should contain email that is not owned by one of the verifications"() {
        given:
        Map properties = [verificationCode: VERIFICATION_CODE, action: REGISTER_ACTION, email: VALID_EMAIL]
        new EmailVerification(properties).save(flush: true)

        expect:
        !new RequestRegistrationCommand(email: VALID_EMAIL).validate()
    }
}
