package club.tempvs.user

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import grails.web.mapping.LinkGenerator
import spock.lang.Specification

class VerifyServiceSpec extends Specification implements ServiceUnitTest<VerifyService>, DomainUnitTest<EmailVerification> {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String VERIFICATION_CODE = 'verificationCode'

    def user = Mock User
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def userService = Mock UserService
    def restResponse = Mock RestResponse
    def profileService = Mock ProfileService
    def restCallService = Mock RestCaller
    def groovyPageRenderer = Mock PageRenderer
    def grailsLinkGenerator = Mock LinkGenerator
    def emailVerification = Mock EmailVerification

    def setup() {
        GroovySpy(EmailVerification, global: true)

        service.restCaller = restCallService
        service.userService = userService
        service.profileService = profileService
        service.groovyPageRenderer = groovyPageRenderer
        service.grailsLinkGenerator = grailsLinkGenerator
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
        def result = service.sendEmailVerification(emailVerification)

        then:
        1 * grailsLinkGenerator.serverBaseURL >> "serverURL"
        1 * groovyPageRenderer.render(_ as Map) >> [:]
        1 * emailVerification.verificationCode
        1 * emailVerification.action
        1 * emailVerification.email
        1 * restCallService.doPost(_ as String, _ as JSON, _ as Map) >> restResponse
        0 * _

        and:
        result == restResponse
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
