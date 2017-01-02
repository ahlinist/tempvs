package com.tempvs.services

import com.tempvs.ajax.AjaxResponseFactory
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(AjaxResponseService)
@Mock([User, UserProfile])
class AjaxResponseServiceSpec extends Specification {
    private static final SUCCESS_MESSAGE = 'Success Message'

    def setup() {
    }

    def cleanup() {
    }

    void "Check composeJsonResponse() for 2 args"() {
        given: 'Mocking the factory'
        def ajaxResponseFactory = Mock(AjaxResponseFactory)
        service.ajaxResponseFactory = ajaxResponseFactory
        def user = Mock(User)

        when: 'Service returned success for 2 args call'
        service.composeJsonResponse(user, SUCCESS_MESSAGE)

        then: 'AjaxResponseFactory is invoked'
        1 * ajaxResponseFactory.newInstance(user, SUCCESS_MESSAGE)
    }

    void "Check composeJsonResponse() for 1 arg"() {
        given: 'Mocking the factory'
        def ajaxResponseFactory = Mock(AjaxResponseFactory)
        service.ajaxResponseFactory = ajaxResponseFactory
        def user = Mock(User)

        when: 'Service returned success for 1 arg'
        service.composeJsonResponse(user)

        then: 'AjaxResponseFactory is invoked'
        1 * ajaxResponseFactory.newInstance(user, null)
    }
}
