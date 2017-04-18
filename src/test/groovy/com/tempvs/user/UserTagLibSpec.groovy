package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(UserTagLib)
class UserTagLibSpec extends Specification {

    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String PROPERTIES = 'properties'

    def userProfile = Mock(UserProfile)

    def setup() {

    }

    def cleanup() {
    }

    void "Test tempvs:fullName"() {
        Map properties = [profile: userProfile]

        when:
        String result = tagLib.fullName(properties)

        then:
        1 * userProfile.firstName >> FIRST_NAME
        1 * userProfile.lastName >> LAST_NAME
        _ * userProfile._
        0 * _

        and:
        result == "${FIRST_NAME} ${LAST_NAME} "
    }
}
