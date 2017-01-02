package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(UserService)
@Mock(UserProfile)
class UserServiceSpec extends Specification {

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)
    }

    def cleanup() {
    }

    void "Check getUser() method for id"() {
        given: 'Mocking the user'
        def user = Mock(User)

        when: 'getUser() is called'
        service.getUser(UnitTestUtils.USER_ID as String)

        then: 'User is queried in the DB'
        1 * User.get(UnitTestUtils.USER_ID) >> user
    }

    void "Check getUser() method for customId"() {
        given: 'Mocking the user'
        def user = Mock(User)

        when: 'getUser() is called'
        service.getUser(UnitTestUtils.CUSTOM_ID)

        then: 'User is queried in the DB'
        1 * UserProfile.findByCustomId(UnitTestUtils.CUSTOM_ID) >> user
    }

    void "Check getUserByEmail()"() {
        given: 'Mocking the user'
        def user = Mock(User)

        when: 'getUserByEmail() is called'
        service.getUserByEmail(UnitTestUtils.EMAIL)

        then: 'User is queried in the DB'
        1 * User.findByEmail(UnitTestUtils.EMAIL) >> user
    }
}
