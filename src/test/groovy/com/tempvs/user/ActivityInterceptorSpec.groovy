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
    private static final String USER_PROFILE = 'userProfile'
    private static final String IMAGE = 'image'
    private static final String VERIFY = 'verify'

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
        controller  | action
        USER        | 'edit'
        USER        | 'updateEmail'
        USER        | 'updatePassword'
        USER        | 'index'
        USER        | 'register'
        USER_PROFILE| 'show'
        USER_PROFILE| 'edit'
        USER_PROFILE| 'updateUserProfile'
        USER_PROFILE| 'updateProfileEmail'
        IMAGE       | 'updateAvatar'
        IMAGE       | 'getAvatar'
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
        AUTH        | 'index'
        VERIFY      | 'registration'
        VERIFY      | 'email'
        VERIFY      | 'profileEmail'
    }
}
