package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
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
    def objectDAOService = Mock ObjectDAOService

    def setup() {
        GroovySpy(EmailVerification, global: true)

        service.mailService = mailService
        service.objectDAOService = objectDAOService
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
        def result = service.createEmailVerification(emailVerification)

        then:
        1 * emailVerification.email >> EMAIL
        1 * emailVerification.setVerificationCode(_ as String)
        1 * objectDAOService.save(emailVerification) >> emailVerification
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
