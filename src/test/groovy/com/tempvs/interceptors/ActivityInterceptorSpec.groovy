package com.tempvs.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ActivityInterceptor)
class ActivityInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test activity interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller:'user', action:'edit')

        then: "The interceptor does match"
        interceptor.doesMatch()
    }

    void "Interceptor doesn't match the excluded actions"() {
        when: "A request doesn't match the 'register' action"
        withRequest(controller:'user', action:'register')

        then: "The interceptor does match"
        !interceptor.doesMatch()

        when: "A request doesn't match the login action"
        withRequest(controller:'user', action:'login')

        then: "The interceptor does match"
        !interceptor.doesMatch()
    }
}
