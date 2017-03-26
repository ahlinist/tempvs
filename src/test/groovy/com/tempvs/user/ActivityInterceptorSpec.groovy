package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ActivityInterceptor)
class ActivityInterceptorSpec extends Specification {
    private static final String AUTH = 'auth'
    private static final String USER = 'user'
    private static final String IMAGE = 'image'

    def setup() {
    }

    def cleanup() {

    }

    void "Test activity interceptor matching"() {
        when:
        withRequest(controller: controller, action: action)

        then:
        interceptor.doesMatch()

        where:
        controller | action
        USER       | 'show'
        USER       | 'edit'
        USER       | 'profile'
        USER       | 'updateEmail'
        USER       | 'updatePassword'
        USER       | 'updateUserProfile'
        USER       | 'updateProfileEmail'
        USER       | 'register'
        USER       | 'verify'

    }

    void "Interceptor doesn't match the excluded actions"() {
        when:
        withRequest(controller: controller, action: action)

        then:
        !interceptor.doesMatch()

        where:
        controller  | action
        AUTH        | 'register'
        AUTH        | 'login'
        IMAGE       | 'updateAvatar'
        IMAGE       | 'getAvatar'
    }
}
