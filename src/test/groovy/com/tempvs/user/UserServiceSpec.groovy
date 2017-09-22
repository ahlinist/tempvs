package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
class UserServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'

    def user = Mock User
    def objectDAO = Mock ObjectDAO
    def grailsUser = Mock GrailsUser
    def userProfile = Mock UserProfile
    def objectFactory = Mock ObjectFactory
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)

        service.springSecurityService = springSecurityService
        service.objectFactory = objectFactory
        service.objectDAO = objectDAO
    }

    def cleanup() {
    }

    void "Test getUser()"() {
        when:
        def result = service.getUser(ID)

        then:
        1 * objectDAO.get(User.class, ID) >> user
        0 * _

        and:
        result == user
    }

    void "Test getCurrentUser()"() {
        when:
        def result = service.getCurrentUser()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User) >> user
        0 * _

        result == user
    }

    void "Test getCurrentUserId()"() {
        when:
        def result = service.getCurrentUserId()

        then:
        1 * springSecurityService.currentUserId >> LONG_ID
        0 * _

        result == LONG_ID
    }

    void "Test getCurrentUserEmail()"() {
        when:
        def result = service.getCurrentUserEmail()

        then:
        1 * springSecurityService.loggedIn >> Boolean.TRUE
        1 * springSecurityService.principal >> grailsUser
        1 * grailsUser.username >> EMAIL
        0 * _

        result == EMAIL
    }

    void "Test getCurrentUserPassword()"() {
        when:
        def result = service.getCurrentUserPassword()

        then:
        1 * springSecurityService.loggedIn >> Boolean.TRUE
        1 * springSecurityService.principal >> grailsUser
        1 * grailsUser.password >> PASSWORD
        0 * _

        result == PASSWORD
    }

    void "Check getUserByEmail()"() {
        when:
        def result = service.getUserByEmail(EMAIL)

        then:
        1 * User.findByEmail(EMAIL) >> user

        and:
        result == user
    }

    void "Test saveUser()"() {
        when:
        def result = service.saveUser(user)

        then:
        1 * user.save()
        0 * _

        and:
        result == user
    }
}
