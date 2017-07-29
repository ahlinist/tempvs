package com.tempvs.user

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import static com.tempvs.tests.utils.TestingUtils.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ClubProfile)
@Mock([User, UserProfile])
class ClubProfileSpec extends Specification {

    private static final String NON_VALID_EMAIL = 'non-valid-email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def user = Mock(User)

    def setup() {
    }

    def cleanup() {
    }

    void "Test firstName nullability"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        params.user = user
        params.firstName = null

        expect:
        !new ClubProfile(params).validate()
    }

    void "Test profileEmail email validation"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        params.user = user
        params.profileEmail = NON_VALID_EMAIL

        expect:
        !new ClubProfile(params).validate()
    }

    void "Test clubProfile creation without a period"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        params.period = null

        expect:
        !new ClubProfile(params).validate()
    }

    void "Test successful clubProfile creation"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        params.user = user

        expect:
        new ClubProfile(params).validate()
    }

    void "User can not be created with numeric profileId"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        params.profileId = NUMERIC_PROFILE_ID

        expect:
        !new ClubProfile(params).validate()
    }

    void "Test toString()"() {
        given:
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()

        expect:
        new ClubProfile(params).toString() ==
                "${FIRST_NAME} ${LAST_NAME} ${NICK_NAME}"
    }
}
