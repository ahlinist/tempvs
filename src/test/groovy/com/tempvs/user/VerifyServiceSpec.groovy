package com.tempvs.user

import grails.plugins.mail.MailService
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class VerifyServiceSpec extends Specification implements ServiceUnitTest<VerifyService>, DomainUnitTest<EmailVerification> {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static String REGISTRATION_ACTION = 'registration'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def user = Mock User
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def mailService = Mock MailService
    def userService = Mock UserService
    def profileService = Mock ProfileService
    def emailVerification = Mock EmailVerification

    def setup() {
        GroovySpy(EmailVerification, global: true)

        service.mailService = mailService
        service.userService = userService
        service.profileService = profileService
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
        1 * userService.getUserByEmail(EMAIL)
        1 * profileService.getProfileByProfileEmail(UserProfile, EMAIL)
        1 * profileService.getProfileByProfileEmail(ClubProfile, EMAIL)
        1 * emailVerification.action >> REGISTRATION_ACTION
        1 * emailVerification.instanceId >> LONG_ID
        1 * emailVerification.setVerificationCode(_ as String)
        1 * emailVerification.save() >> emailVerification
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

    void "Test isEmailUnique()"() {
        when:
        def result = service.isEmailUnique(EMAIL, REGISTRATION_ACTION, LONG_ID)

        then:
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * profileService.getProfileByProfileEmail(UserProfile, EMAIL)
        1 * profileService.getProfileByProfileEmail(ClubProfile, EMAIL)
        0 * _

        and:
        result == Boolean.FALSE
    }
}
