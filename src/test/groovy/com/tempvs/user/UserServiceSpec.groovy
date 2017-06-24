package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.tests.utils.TestingUtils
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, UserProfile])
class UserServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'

    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def objectFactory = Mock(ObjectFactory)
    def objectDAO = Mock(ObjectDAO)
    def grailsUser = Mock(GrailsUser)

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
        1 * springSecurityService.principal >> grailsUser
        1 * grailsUser.username >> EMAIL
        0 * _

        result == EMAIL
    }

    void "Test getCurrentUserPassword()"() {
        when:
        def result = service.getCurrentUserPassword()

        then:
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

    void "Check createUser() creation"() {
        given:
        Map properties = TestingUtils.DEFAULT_USER_PROPS

        when:
        def result = service.createUser(properties)

        then:
        1 * objectFactory.create(User.class) >> user
        1 * objectFactory.create(UserProfile.class) >> userProfile
        1 * springSecurityService.encodePassword(TestingUtils.DEFAULT_USER_PROPS.password) >> PASSWORD
        1 * user.setEmail(TestingUtils.DEFAULT_USER_PROPS.email)
        1 * user.setPassword(PASSWORD)
        1 * userProfile.setFirstName(TestingUtils.DEFAULT_USER_PROPS.firstName)
        1 * userProfile.setLastName(TestingUtils.DEFAULT_USER_PROPS.lastName)
        1 * user.setUserProfile(userProfile)
        1 * user.save()
        0 * _

        and:
        result == user
    }

    void "Test updateEmail()"() {
        when:
        def result = service.updateEmail(LONG_ID, EMAIL)

        then:
        1 * objectDAO.get(User, LONG_ID) >> user
        1 * user.setEmail(EMAIL)
        1 * user.save()

        and:
        result == user
    }

    void "Test updatePassword()"() {
        when:
        def result = service.updatePassword(PASSWORD)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User) >> user
        1 * springSecurityService.encodePassword(PASSWORD) >> PASSWORD
        1 * user.setPassword(PASSWORD)
        1 * user.save()

        and:
        result == user
    }

    void "Check updateLastActive()"() {
        when:
        service.updateLastActive()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User) >> user
        1 * user.setLastActive(_ as Date)
        1 * user.save()
        0 * _
    }
}
