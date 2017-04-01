package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(UserTagLib)
class UserTagLibSpec extends Specification {

    private static final String USER_PROFILE = 'userProfile'
    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'

    def user = Mock(User)
    def userProfile = Mock(UserProfile)

    def setup() {

    }

    def cleanup() {
    }

    void "Test tempvs:fullName"() {
        given:
        Map properties = [user: user]

        when:
        String result = tagLib.fullName(properties)

        then:
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(FIRST_NAME) >> FIRST_NAME
        1 * userProfile.getProperty(LAST_NAME) >> LAST_NAME
        0 * _

        and:
        result == "${FIRST_NAME} ${LAST_NAME}"
    }
}
