package club.tempvs.user

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import grails.web.mapping.LinkGenerator
import org.springframework.http.HttpStatus
import spock.lang.Specification

class VerifyServiceSpec extends Specification implements ServiceUnitTest<VerifyService>, DomainUnitTest<EmailVerification> {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String REGISTRATION_ACTION = 'registration'

    def user = Mock User
    def profile = Mock Profile
    def userService = Mock UserService
    def restResponse = Mock RestResponse
    def profileService = Mock ProfileService
    def restCallService = Mock RestCaller
    def groovyPageRenderer = Mock PageRenderer
    def grailsLinkGenerator = Mock LinkGenerator
    def emailVerification = Mock EmailVerification

    def setup() {
        service.restCaller = restCallService
        service.userService = userService
        service.profileService = profileService
        service.groovyPageRenderer = groovyPageRenderer
        service.grailsLinkGenerator = grailsLinkGenerator
    }

    def cleanup() {
    }

    void "Test successful creation of email verification"() {
        when:
        def result = service.createEmailVerification(emailVerification)

        then:
        1 * emailVerification.email >> EMAIL
        1 * userService.getUserByEmail(EMAIL)
        1 * profileService.getProfilesByProfileEmail(EMAIL)
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
        1 * restCallService.doPost(_ as String, _, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> HttpStatus.OK
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test isEmailUnique()"() {
        when:
        def result = service.isEmailUnique(EMAIL, REGISTRATION_ACTION, LONG_ID)

        then:
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * profileService.getProfilesByProfileEmail(EMAIL)
        0 * _

        and:
        result == Boolean.FALSE
    }
}
