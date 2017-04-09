package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(UserService)
class UserServiceSpec extends Specification {
    private static final String ONE = '1'
    private static final Long LONG_ID = 1L
    private static final String PROFILE_ID = 'profileId'
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'
    private static final String USER_PROFILE = 'userProfile'

    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)

        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Check getUser() method for id"() {
        when:
        def result = service.getUser(ONE)

        then:
        1 * UserProfile.findByProfileId(ONE) >> null
        1 * User.get(_) >> user
        0 * _

        and:
        result == user
    }

    void "Check getUser() method for profileId"() {
        when:
        def result = service.getUser(PROFILE_ID)

        then:
        1 * UserProfile.findByProfileId(PROFILE_ID) >> userProfile
        1 * userProfile.getProperty(USER) >> user

        and:
        result == user
    }

    void "Check getUserByEmail()"() {
        when:
        def result = service.getUserByEmail(EMAIL)

        then:
        1 * User.findByEmail(EMAIL) >> user

        and:
        result == user
    }

    void "Check createUser() creation"() {
        given:
        Map properties = [:]

        when:
        def result = service.createUser(properties)

        then:
        1 * new User(properties) >> user
        1 * user.setProperty(PASSWORD, PASSWORD)
        1 * user.getProperty(PASSWORD) >> PASSWORD
        1 * user.getProperty('springSecurityService') >> springSecurityService
        1 * springSecurityService.encodePassword(_) >> PASSWORD
        1 * user.setProperty(USER_PROFILE, userProfile)
        1 * new UserProfile(properties) >> userProfile
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Test updateEmail()"() {
        when:
        def result = service.updateEmail(LONG_ID, EMAIL)

        then:
        1 * User.get(LONG_ID) >> user
        1 * user.setEmail(EMAIL)
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Test updatePassword()"() {
        when:
        def result = service.updatePassword(PASSWORD)

        then:
        1 * springSecurityService.currentUser >> user
        1 * springSecurityService.encodePassword(PASSWORD) >> PASSWORD
        1 * user.setPassword(PASSWORD)
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Check updateLastActive()"() {
        when:
        service.updateLastActive()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.setLastActive(_ as Date)
        1 * user.save([flush: true])
    }
}
