package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.utils.TestingUtils
import com.tempvs.tests.utils.user.WithUser
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthController)
@Mock([User, UserProfile])
class AuthControllerSpec extends Specification implements WithUser {
    private static final String INCORRECT = 'incorrect'
    private static final String NO_SUCH_USER_LOGIN = 'user.login.noSuchUser.message'
    private static final String MOCKED_RESPONSE = 'mocked_response'
    private static final String SHOW_PAGE_URL = '/user/show'

    def setup() {
        controller.ajaxResponseService = [composeJsonResponse: { obj, string = null ->
            [success: MOCKED_RESPONSE] as JSON
        }]

        controller.userService = [
                getUserByEmail:{ email ->
                    if (email == TestingUtils.EMAIL) {
                        return user
                    }
                }
        ]

        controller.passwordEncoder = [isPasswordValid: { userPass, inputPass, arg ->
            if (userPass == inputPass) {
                true
            }
        }]
    }

    def cleanup() {
    }

    void "Render register page"() {
        when: 'Call register() action'
        controller.register()

        then: 'No modelAndView objects returned and no redirects happen'
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Passing params into register form"() {
        when: 'Passing register params'
        params.isAjaxRequest = Boolean.TRUE
        controller.register()

        then: 'JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Render login page"() {
        when: 'Call login() action'
        controller.login()

        then: 'No modelAndView objects returned and no redirects happen'
        controller.modelAndView == null
        response.redirectedUrl == null
    }

    void "Pass invalid params into login form"() {
        when: 'Passing non-valid params'
        params.isAjaxRequest = Boolean.TRUE
        params.email = TestingUtils.EMAIL
        controller.login()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Pass valid params but with incorrect email into login form"() {
        when: 'Passing email for existent user'
        params.isAjaxRequest = Boolean.TRUE
        params.email = INCORRECT + TestingUtils.EMAIL
        params.password = TestingUtils.PASSWORD
        controller.login()

        then: 'JSON response with the corresponding message returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        response.text == noSuchUserJson as String
    }

    void "Pass valid params but with incorrect pass into login form"() {
        when: 'Passing email for existent user'
        params.isAjaxRequest = Boolean.TRUE
        params.email = TestingUtils.EMAIL
        params.password = INCORRECT + TestingUtils.PASSWORD
        controller.login()

        then: 'JSON response with the corresponding message returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        response.text == noSuchUserJson as String
    }

    void "Pass valid and correct params into login form"() {
        given: 'Mocking springSecurityService'
        controller.springSecurityService = [reauthenticate:{arg1, arg2 -> }]

        when: 'Passing email for existent user'
        params.isAjaxRequest = Boolean.TRUE
        params.email = TestingUtils.EMAIL
        params.password = TestingUtils.PASSWORD
        controller.login()

        then: 'JSON response with show page redirect returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        response.text == showPageRedirectJson as String
    }

    private static JSON getShowPageRedirectJson() {
        [redirect: SHOW_PAGE_URL] as JSON
    }

    private static JSON getNoSuchUserJson() {
        [messages: [NO_SUCH_USER_LOGIN]] as JSON
    }
}
