package com.tempvs.user

import com.tempvs.user.User
import com.tempvs.user.verification.EmailVerification
import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.UserService
import com.tempvs.user.AuthController
import com.tempvs.user.LoginCommand
import com.tempvs.user.RequestRegistrationCommand
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.security.authentication.encoding.PasswordEncoder
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthController)
class AuthControllerSpec extends Specification {
    private static final String EMAIL = 'authUnitTest@email.com'
    private static final String PASSWORD = 'password'
    private static final String REGISTER_ACTION = 'register'
    private static final String NO_SUCH_USER_MESSAGE = 'auth.login.noSuchUser.message'
    private static final String SHOW_PAGE_URI = '/user/show'

    def emailVerification = Mock(EmailVerification)
    def requestRegistrationCommand = Mock(RequestRegistrationCommand)
    def ajaxResponseService = Mock(AjaxResponseService)
    def userService = Mock(UserService)
    def json = Mock(JSON)
    def passwordEncoder = Mock(PasswordEncoder)
    def springSecurityService = Mock(SpringSecurityService)
    def loginCommand = Mock(LoginCommand)
    def user = Mock(User)

    def setup() {
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.passwordEncoder = passwordEncoder
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Render register page"() {
        when: 'Register() action is called'
        controller.register()

        then: 'No modelAndView objects returned and no redirects happen'
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Testing register() action with invalid params"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.register(requestRegistrationCommand)

        then: 'JSON response returned'
        1 * requestRegistrationCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(requestRegistrationCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing register() action with valid params"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.register(requestRegistrationCommand)

        then: 'JSON response returned'
        1 * requestRegistrationCommand.validate() >> Boolean.TRUE
        1 * requestRegistrationCommand.getEmail() >> EMAIL
        1 * userService.createEmailVerification(['action': REGISTER_ACTION, 'email': EMAIL]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, _ as String) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Render login page"() {
        when: 'Call login() action'
        controller.login()

        then: 'No modelAndView objects returned and no redirects happen'
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Testing login() for non-valid params"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then: 'JSON response returned'
        1 * loginCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(loginCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Testing login() for non-existing user"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then: 'JSON response returned'
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> null
        0 * _
        response.json.messages == [NO_SUCH_USER_MESSAGE]
    }

    void "Testing login() for incorrect password"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then: 'JSON response returned'
        1 * loginCommand.validate() >> Boolean.TRUE
        1 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.getProperty(PASSWORD) >> PASSWORD
        1 * loginCommand.getPassword() >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.FALSE
        0 * _
        response.json.messages == [NO_SUCH_USER_MESSAGE]
    }

    void "Testing login() for correct params"() {
        when: 'Passing command mock'
        params.isAjaxRequest = Boolean.TRUE
        controller.login(loginCommand)

        then: 'JSON response returned'
        1 * loginCommand.validate() >> Boolean.TRUE
        2 * loginCommand.getEmail() >> EMAIL
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * user.getProperty(PASSWORD) >> PASSWORD
        2 * loginCommand.getPassword() >> PASSWORD
        1 * passwordEncoder.isPasswordValid(PASSWORD, PASSWORD, null) >> Boolean.TRUE
        1 * springSecurityService.reauthenticate(EMAIL, PASSWORD)
        0 * _
        response.json.redirect == SHOW_PAGE_URI
    }
}
