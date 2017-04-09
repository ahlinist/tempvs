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
        given:
        Map properties = [profile: userProfile]

        when:
        String result = tagLib.fullName(properties)

        then:
        1 * userProfile.getProperty(PROPERTIES) >> [firstName: FIRST_NAME, lastName: LAST_NAME]
        0 * _

        and:
        result == "${FIRST_NAME} ${LAST_NAME} "
    }
}
