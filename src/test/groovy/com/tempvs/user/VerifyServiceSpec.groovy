package com.tempvs.user

import grails.plugins.mail.MailService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VerifyService)
@Mock([EmailVerification])
class VerifyServiceSpec extends Specification {

    private static final String EMAIL = 'email'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def mailService = Mock MailService
    def emailVerification = Mock EmailVerification

    def setup() {
        GroovySpy(EmailVerification, global: true)

        service.mailService = mailService
    }

    def cleanup() {
    }

    void "Test getVerification()"() {
        when:
        def result = service.getVerification(VERIFICATION_CODE)

        then:
        1 * EmailVerification.findByVerificationCode(VERIFICATION_CODE) >> emailVerification
        0 * _

        and:
        result == emailVerification
    }

    void "Test successful creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created and mail sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save() >> emailVerification
        0 * _

        and:
        result == emailVerification
    }

    void "Test failed creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created, not saved and not sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save() >> null
        0 * _

        and:
        result == emailVerification
    }

    void "Test sendEmailVerification()"() {
        when:
        service.sendEmailVerification(emailVerification)

        then:
        1 * mailService.sendMail(_)
        0 * _
    }
}
