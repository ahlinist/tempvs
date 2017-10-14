package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
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
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def grailsUser = Mock GrailsUser
    def userProfile = Mock UserProfile
    def objectDAOService = Mock ObjectDAOService
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)

        service.objectDAOService = objectDAOService
        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Test getUser()"() {
        when:
        def result = service.getUser(ID)

        then:
        1 * objectDAOService.get(User.class, ID) >> user
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
        0 * _

        and:
        result == user
    }

    void "Test editUserField()"() {
        when:
        def result = service.editUserField(user, EMAIL, FIELD_VALUE)

        then:
        1 * user.setEmail(FIELD_VALUE)
        1 * objectDAOService.save(user) >> user
        0 * _

        and:
        result == user
    }

    void "Test register()"() {
        given:

        when:
        def result = service.register(user)

        then:
        1 * objectDAOService.save(user) >> user
        0 * _

        and:
        result == user
    }
}
