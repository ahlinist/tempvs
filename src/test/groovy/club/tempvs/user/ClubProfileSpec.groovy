package club.tempvs.user

import club.tempvs.item.Passport
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import static club.tempvs.tests.utils.TestingUtils.*

class ClubProfileSpec extends Specification implements DomainUnitTest<ClubProfile> {

    private static final String NON_VALID_EMAIL = 'non-valid-email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def user = Mock User
    def passport = Mock Passport

    def profileService = Mock(ProfileService) {
        isProfileEmailUnique(_ as Profile, _ as String) >> Boolean.TRUE
    }

    ClubProfile clubProfile

    def setup() {
        GroovySpy(Passport, global: true)
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
        when:
        def result = clubProfile.toString()

        then:
        result == "${FIRST_NAME} ${LAST_NAME} ${NICK_NAME}"
    }
}
