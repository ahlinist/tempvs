package com.tempvs.auth

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.EmailVerification
import com.tempvs.user.User
import com.tempvs.user.UserService
import com.tempvs.user.VerifyService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.web.mapping.LinkGenerator
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthController)
class AuthControllerSpec extends Specification {
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'
    private static final String REGISTER_ACTION = 'registration'
    private static final String NO_SUCH_USER_MESSAGE = 'No user with such id found.'
    private static final String PROFILE_PAGE_URI = '/profile'

    def emailVerification = Mock(EmailVerification)
    def requestRegistrationCommand = Mock(RequestRegistrationCommand)
    def ajaxResponseService = Mock(AjaxResponseService)
    def userService = Mock(UserService)
    def verifyService = Mock(VerifyService)
    def json = Mock(JSON)
    def passwordEncoder = Mock(PasswordEncoder)
    def springSecurityService = Mock(SpringSecurityService)
    def loginCommand = Mock(LoginCommand)
    def user = Mock(User)
    def grailsLinkGenerator = Mock(LinkGenerator)

    def setup() {
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.passwordEncoder = passwordEncoder
        controller.springSecurityService = springSecurityService
        controller.verifyService = verifyService
        controller.grailsLinkGenerator = grailsLinkGenerator
    }

    def cleanup() {
    }

    void "Testing index() page rendering"() {
        when:
        controller.index()

        then:
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Testing register() action with invalid params"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(requestRegistrationCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing register() action with valid params"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.TRUE
        1 * requestRegistrationCommand.getEmail() >> EMAIL
        1 * verifyService.createEmailVerification(['action': REGISTER_ACTION, 'email': EMAIL]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, _ as String) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-valid params"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(loginCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-existing user"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> null
        0 * _
        response.json.messages == [NO_SUCH_USER_MESSAGE]
    }

    void "Testing login() for incorrect password"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.getPassword() >> PASSWORD
        1 * loginCommand.getPassword() >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.FALSE
        0 * _
        response.json.messages == [NO_SUCH_USER_MESSAGE]
    }

    void "Testing login() for correct params"() {
        given:
        Map linkGeneratorMap = ['controller':'profile']

        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        2 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.getPassword() >> PASSWORD
        2 * loginCommand.getPassword() >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> PROFILE_PAGE_URI
        0 * _
        response.json.redirect == PROFILE_PAGE_URI
    }
}
