package com.tempvs.user

import grails.plugins.mail.MailService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VerifyService)
class VerifyServiceSpec extends Specification {

    private static final String VERIFICATION_CODE = 'verificationCode'
    private static final String EMAIL = 'email'

    def mailService = Mock(MailService)
    def emailVerification = Mock(EmailVerification)

    def setup() {
        GroovySpy(EmailVerification, global: true)

        service.mailService = mailService
    }

    def cleanup() {
    }

    void "Check getVerification()"() {
        when:
        def result = service.getVerification(VERIFICATION_CODE)

        then:
        1 * EmailVerification.findByVerificationCode(VERIFICATION_CODE) >> emailVerification

        and:
        result == emailVerification
    }

    void "Check successful creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created and mail sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save([flush: true]) >> emailVerification
        1 * mailService.sendMail(_)

        and:
        result == emailVerification
    }

    void "Check failed creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created, not saved and not sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save([flush: true])

        and:
        result == emailVerification
    }
}
