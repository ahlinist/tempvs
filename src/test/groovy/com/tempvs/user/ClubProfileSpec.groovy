package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification

import static com.tempvs.tests.utils.TestingUtils.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ClubProfile)
class ClubProfileSpec extends Specification {

    private static final String NON_VALID_EMAIL = 'non-valid-email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def user = Mock(User)

    def profileService = Mock(ProfileService) {
        isProfileEmailUnique(_ as BaseProfile, _ as String) >> Boolean.TRUE
    }

    ClubProfile clubProfile

    def setup() {
        Map params = DEFAULT_CLUB_PROFILE_PROPS.clone()
        clubProfile = new ClubProfile(params + [user: user, profileService: profileService])
    }

    def cleanup() {
    }

    void "Test firstName nullability"() {
        given:
        clubProfile.firstName = null

        expect:
        !clubProfile.validate()
    }

    void "Test profileEmail email validation"() {
        given:
        clubProfile.profileEmail = NON_VALID_EMAIL

        expect:
        !clubProfile.validate()
    }

    void "Test clubProfile creation without a period"() {
        given:
        clubProfile.period = null

        expect:
        !clubProfile.validate()
    }

    void "Test successful clubProfile creation"() {
        given:
        clubProfile.validate()

        expect:
        clubProfile.validate()
    }

    void "User can not be created with numeric profileId"() {
        given:
        clubProfile.profileId = NUMERIC_PROFILE_ID

        expect:
        !clubProfile.validate()
    }

    void "Test toString()"() {
        expect:
        clubProfile.toString() == "${FIRST_NAME} ${LAST_NAME} ${NICK_NAME}"
    }
}
