package club.tempvs.auth

import club.tempvs.rest.RestResponse
import club.tempvs.user.EmailVerification
import club.tempvs.user.User
import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.UserService
import club.tempvs.user.VerifyService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import grails.web.mapping.LinkGenerator
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices
import spock.lang.Specification

class AuthControllerSpec extends Specification implements ControllerUnitTest<AuthController> {

    private static final String EMAIL = 'email'
    private static final String TEST_URI = '/test'
    private static final String GET_METHOD = 'GET'
    private static final String PROFILE = 'profile'
    private static final String POST_METHOD = 'POST'
    private static final String PASSWORD = 'password'
    private static final String LOGIN_PAGE_URI = '/auth/index'
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final String NO_SUCH_USER_CODE = 'auth.login.noSuchUser.message'

    def user = Mock User
    def json = Mock JSON
    def userService = Mock UserService
    def loginCommand = Mock LoginCommand
    def restResponse = Mock RestResponse
    def verifyService = Mock VerifyService
    def authentication = Mock Authentication
    def passwordEncoder = Mock PasswordEncoder
    def grailsLinkGenerator = Mock LinkGenerator
    def emailVerification = Mock EmailVerification
    def ajaxResponseHelper = Mock AjaxResponseHelper
    def springSecurityService = Mock SpringSecurityService
    def rememberMeServices = Mock TokenBasedRememberMeServices
    def requestRegistrationCommand = Mock RequestRegistrationCommand

    def setup() {
        controller.userService = userService
        controller.verifyService = verifyService
        controller.passwordEncoder = passwordEncoder
        controller.rememberMeServices = rememberMeServices
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Testing index() page rendering"() {
        when:
        request.method = GET_METHOD
        controller.index()

        then:
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Testing register() action with invalid params"() {
        when:
        request.method = POST_METHOD
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(requestRegistrationCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing register() action with valid params"() {
        when:
        request.method = POST_METHOD
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.TRUE
        1 * requestRegistrationCommand.email >> EMAIL
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification) >> restResponse
        1 * restResponse.statusCode >> 200
        1 * ajaxResponseHelper.renderFormMessage(Boolean.TRUE, 'auth.register.sent.message') >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-valid params"() {
        when:
        request.method = POST_METHOD
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(loginCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-existing user"() {
        when:
        request.method = POST_METHOD
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * loginCommand.password >> PASSWORD
        1 * userService.getUserByEmail(EMAIL) >> null
        1 * ajaxResponseHelper.renderFormMessage(Boolean.FALSE, NO_SUCH_USER_CODE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for incorrect password"() {
        when:
        request.method = POST_METHOD
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * user.password >> PASSWORD
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * loginCommand.password >> PASSWORD
        1 * passwordEncoder.matches(PASSWORD, PASSWORD) >> Boolean.FALSE
        1 * ajaxResponseHelper.renderFormMessage(Boolean.FALSE, NO_SUCH_USER_CODE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for correct params from login page"() {
        given:
        request.method = POST_METHOD
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addHeader('referer', LOGIN_PAGE_URI)
        Map linkGeneratorMap = ['controller': PROFILE]

        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * loginCommand.password >> PASSWORD
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.password >> PASSWORD
        1 * passwordEncoder.matches(PASSWORD, PASSWORD) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        1 * loginCommand.remember >> Boolean.FALSE
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> PROFILE_PAGE_URI
        1 * ajaxResponseHelper.renderRedirect(PROFILE_PAGE_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for correct params"() {
        given:
        request.method = POST_METHOD
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addHeader('referer', TEST_URI)

        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * loginCommand.password >> PASSWORD
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.password >> PASSWORD
        1 * passwordEncoder.matches(PASSWORD, PASSWORD) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        1 * loginCommand.remember >> Boolean.TRUE
        1 * springSecurityService.authentication >> authentication
        1 * rememberMeServices.onLoginSuccess(_ as GrailsMockHttpServletRequest, _ as GrailsMockHttpServletResponse, authentication)
        1 * ajaxResponseHelper.renderRedirect(TEST_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test logout()"() {
        given:
        controller.request.addHeader('referer', TEST_URI)

        when:
        controller.logout()

        then:
        1 * rememberMeServices.logout(_ as GrailsMockHttpServletRequest, _ as GrailsMockHttpServletResponse, null)
        0 * _

        and:
        response.redirectedUrl == TEST_URI
    }
}
