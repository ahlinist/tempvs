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
        when:"A request matches the interceptor"
            withRequest(controller:"activity")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
