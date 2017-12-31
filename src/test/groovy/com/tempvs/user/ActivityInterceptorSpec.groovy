package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class ActivityInterceptorSpec extends Specification implements InterceptorUnitTest<ActivityInterceptor> {

    private static final String AUTH = 'auth'
    private static final String USER = 'user'
    private static final String ITEM = 'item'
    private static final String IMAGE = 'image'
    private static final String SOURCE = 'source'
    private static final String VERIFY = 'verify'
    private static final String PROFILE = 'profile'
    private static final String LAST_ACTIVE = 'lastActive'

    def user = Mock User
    def userService = Mock UserService
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        interceptor.userService = userService
        interceptor.springSecurityService = springSecurityService
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
        1 * springSecurityService.loggedIn >> Boolean.TRUE
        1 * userService.currentUser >> user
        1 * userService.editUserField(user, LAST_ACTIVE, _ as Date) >> user
        0 * _

        and:
        result == Boolean.TRUE
    }
}
