package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.item.ItemStash
import com.tempvs.tests.utils.TestingUtils
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, UserProfile])
class UserServiceSpec extends Specification {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'

    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def itemStash = Mock(ItemStash)
    def objectFactory = Mock(ObjectFactory)
    def objectDAO = Mock(ObjectDAO)

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)

        service.springSecurityService = springSecurityService
        service.objectFactory = objectFactory
        service.objectDAO = objectDAO
    }

    def cleanup() {
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
        1 * objectFactory.create(ItemStash.class) >> itemStash
        1 * springSecurityService.encodePassword(TestingUtils.DEFAULT_USER_PROPS.password) >> PASSWORD
        1 * user.setEmail(TestingUtils.DEFAULT_USER_PROPS.email)
        1 * user.setPassword(PASSWORD)
        1 * userProfile.setFirstName(TestingUtils.DEFAULT_USER_PROPS.firstName)
        1 * userProfile.setLastName(TestingUtils.DEFAULT_USER_PROPS.lastName)
        1 * user.setUserProfile(userProfile)
        1 * user.setItemStash(itemStash)
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
