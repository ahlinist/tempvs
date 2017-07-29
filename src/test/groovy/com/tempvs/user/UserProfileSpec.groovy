package com.tempvs.user

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import static com.tempvs.tests.utils.TestingUtils.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
@Mock([User, ClubProfile])
class UserProfileSpec extends Specification {

    private static final String NOT_EMAIL = 'not email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def setup() {
    }

    def cleanup() {
    }

    void "Test fails when firstName is missing"() {
        given:
        Map props = DEFAULT_USER_PROPS.clone()
        props.firstName = null

        expect:
        !createUser(props)
    }

    void "Test fails when lastName is missing"() {
        given:
        Map props = DEFAULT_USER_PROPS.clone()
        props.lastName = null

        expect:
        !createUser(props)
    }

    void "User with incorrect email is not created"() {
        given:
        Map props = DEFAULT_USER_PROPS.clone()
        props.email = NOT_EMAIL

        expect:
        !createUser(props)
    }

    void "User can not be created with numeric profileId"() {
        given:
        Map props = DEFAULT_USER_PROPS.clone()
        props.profileId = NUMERIC_PROFILE_ID

        expect:
        !createUser(props)
    }

    void "Numeric profileId can't be set to existent user"() {
        given:
        User user = createUser()

        when:
        UserProfile userProfile = user.userProfile
        userProfile.profileId = NUMERIC_PROFILE_ID

        then:
        !userProfile.validate()
    }

    void "Test getIdentifier()"() {
        given:
        User user = createUser()

        when:
        UserProfile userProfile = user.userProfile

        then:
        userProfile.identifier == PROFILE_ID
    }
}
