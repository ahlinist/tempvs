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
import org.springframework.context.MessageSource
import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthController)
class AuthControllerSpec extends Specification {

    private static final String EMAIL = 'email'
    private static final String TEST_URI = '/test'
    private static final String PROFILE = 'profile'
    private static final String PASSWORD = 'password'
    private static final String LOGIN_PAGE_URI = '/auth/index'
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final String REGISTER_ACTION = 'registration'
    private static final String NO_SUCH_USER_CODE = 'auth.login.noSuchUser.message'

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
    def messageSource = Mock(MessageSource)

    def setup() {
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.passwordEncoder = passwordEncoder
        controller.springSecurityService = springSecurityService
        controller.verifyService = verifyService
        controller.messageSource = messageSource
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
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(requestRegistrationCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing register() action with valid params"() {
        when:
        controller.register(requestRegistrationCommand)

        then:
        1 * requestRegistrationCommand.validate() >> Boolean.TRUE
        1 * requestRegistrationCommand.email >> EMAIL
        1 * verifyService.createEmailVerification(['action': REGISTER_ACTION, 'email': EMAIL]) >> emailVerification
        1 * ajaxResponseService.renderValidationResponse(emailVerification, _ as String) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-valid params"() {
        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(loginCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-existing user"() {
        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> null
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SUCH_USER_CODE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for incorrect password"() {
        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.email >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.password >> PASSWORD
        1 * loginCommand.password >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.FALSE
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SUCH_USER_CODE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for correct params from login page"() {
        given:
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addHeader('referer', LOGIN_PAGE_URI)
        Map linkGeneratorMap = ['controller': PROFILE]

        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        2 * loginCommand.email >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.password >> PASSWORD
        2 * loginCommand.password >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        1 * grailsLinkGenerator.link(linkGeneratorMap) >> PROFILE_PAGE_URI
        1 * ajaxResponseService.renderRedirect(PROFILE_PAGE_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for correct params"() {
        given:
        controller.grailsLinkGenerator = grailsLinkGenerator
        controller.request.addHeader('referer', TEST_URI)

        when:
        controller.login(loginCommand)

        then:
        1 * loginCommand.validate() >> Boolean.TRUE
        2 * loginCommand.email >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.password >> PASSWORD
        2 * loginCommand.password >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        1 * ajaxResponseService.renderRedirect(TEST_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test logout()"() {
        given:
        controller.request.addHeader('referer', TEST_URI)

        when:
        controller.logout()

        then:
        response.redirectedUrl == TEST_URI
    }
}
