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
    private static final String ITEM = 'item'
    private static final String IMAGE = 'image'
    private static final String SOURCE = 'source'
    private static final String VERIFY = 'verify'
    private static final String PROFILE = 'profile'

    def userService = Mock(UserService)

    def setup() {
        interceptor.userService = userService
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
        PROFILE     | 'index'
        PROFILE     | 'userProfile'
        PROFILE     | 'clubProfile'
        PROFILE     | 'switchProfile'
        PROFILE     | 'edit'
        PROFILE     | 'list'
        PROFILE     | 'create'
        PROFILE     | 'updateUserProfile'
        PROFILE     | 'updateClubProfile'
        PROFILE     | 'updateProfileEmail'
        ITEM        | 'stash'
        ITEM        | 'group'
        ITEM        | 'createGroup'
        SOURCE      | 'index'
        SOURCE      | 'group'
        SOURCE      | 'show'
        SOURCE      | 'createSource'
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
        IMAGE       | 'get'
    }

    void "Test before()"() {
        when:
        Boolean result = interceptor.before()

        then:
        1 * userService.updateLastActive()
        0 * _

        and:
        result == Boolean.TRUE
    }
}
