package com.tempvs.user

import com.tempvs.tests.utils.TestingUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ClubProfile)
@Mock([User, UserProfile])
class ClubProfileSpec extends Specification {

    private static final String NON_VALID_EMAIL = 'non-valid-email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def setup() {
    }

    def cleanup() {
    }

    void "Test firstName nullability"() {
        given:
        User user = TestingUtils.createUser()
        Map props = TestingUtils.DEFAULT_CLUB_PROFILE_PROPS.clone()
        props.firstName = null

        expect:
        !TestingUtils.addClubProfile(user, props)
    }

    void "Test profileEmail email validation"() {
        given:
        User user = TestingUtils.createUser()
        Map props = TestingUtils.DEFAULT_CLUB_PROFILE_PROPS.clone()
        props.profileEmail = NON_VALID_EMAIL

        expect:
        !TestingUtils.addClubProfile(user, props)
    }

    void "Test successful clubProfile creation"() {
        given:
        User user = TestingUtils.createUser()

        expect:
        TestingUtils.addClubProfile(user)
    }

    void "User can not be created with numeric profileId"() {
        given:
        User user = TestingUtils.createUser()
        Map props = TestingUtils.DEFAULT_CLUB_PROFILE_PROPS.clone()
        props.profileEmail = NON_VALID_EMAIL
        props.profileId = NUMERIC_PROFILE_ID

        expect:
        !TestingUtils.addClubProfile(user, props)
    }
}
