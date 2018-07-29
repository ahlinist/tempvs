package club.tempvs.user

import club.tempvs.item.Passport
import club.tempvs.periodization.Period
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import static club.tempvs.tests.utils.TestingUtils.FIRST_NAME
import static club.tempvs.tests.utils.TestingUtils.LAST_NAME
import static club.tempvs.tests.utils.TestingUtils.NICK_NAME

class ProfileSpec extends Specification implements DomainUnitTest<Profile> {

    private static final String INVALID_EMAIL = 'invalid-email'
    private static final String VALID_EMAIL = 'valid@email.com'
    private static final String VALID_PROFILE_ID = 'myId'
    private static final String INVALID_PROFILE_ID = '123456'

    def user = Mock User
    def passport = Mock Passport

    def profileService = Mock(ProfileService) {
        isProfileEmailUnique(_ as Profile, _ as String) >> Boolean.TRUE
    }

    def setup() {
        GroovySpy(Passport, global: true)
    }

    def cleanup() {
    }

    void "Test userProfile with basic configuration is valid"() {
        given:
        populateUserProfile()

        expect:
        domain.validate()
    }

    void "Test userProfile without user"() {
        given:
        populateUserProfile()
        domain.user = null

        expect:
        !domain.validate()
    }

    void "Test userProfile without firstName"() {
        given:
        populateUserProfile()
        domain.firstName = null

        expect:
        !domain.validate()
    }

    void "Test userProfile with profileEmail"() {
        given:
        populateUserProfile()
        domain.profileEmail = VALID_EMAIL

        expect:
        domain.validate()
    }

    void "Test userProfile with invalid profileEmail"() {
        given:
        populateUserProfile()
        domain.profileEmail = INVALID_EMAIL

        expect:
        !domain.validate()
    }

    void "Test userProfile with valid profileId"() {
        given:
        populateUserProfile()
        domain.profileId = VALID_PROFILE_ID

        expect:
        domain.validate()
    }

    void "Test userProfile with invalid profileId"() {
        given:
        populateUserProfile()
        domain.profileId = INVALID_PROFILE_ID

        expect:
        !domain.validate()
    }

    void "Test userProfile toString()"() {
        given:
        populateUserProfile()

        expect:
        domain.toString() == FIRST_NAME
    }

    void "Test userProfile toString() with last name"() {
        given:
        populateUserProfile()
        domain.lastName = LAST_NAME

        expect:
        domain.toString() == "${FIRST_NAME} ${LAST_NAME}"
    }

    //Test clubProfile
    void "Test clubProfile with basic configuration is valid"() {
        given:
        populateClubProfile()

        expect:
        domain.validate()
    }

    void "Test clubProfile without period"() {
        given:
        populateClubProfile()
        domain.period = null

        expect:
        !domain.validate()
    }

    void "Test clubProfile without firstName"() {
        given:
        populateClubProfile()
        domain.firstName = null

        expect:
        !domain.validate()
    }

    void "Test clubProfile without user"() {
        given:
        populateClubProfile()
        domain.user = null

        expect:
        !domain.validate()
    }

    void "Test clubProfile with profileEmail"() {
        given:
        populateClubProfile()
        domain.profileEmail = VALID_EMAIL

        expect:
        domain.validate()
    }

    void "Test clubProfile with invalid profileEmail"() {
        given:
        populateClubProfile()
        domain.profileEmail = INVALID_EMAIL

        expect:
        !domain.validate()
    }

    void "Test clubProfile with valid profileId"() {
        given:
        populateClubProfile()
        domain.profileId = VALID_PROFILE_ID

        expect:
        domain.validate()
    }

    void "Test clubProfile with invalid profileId"() {
        given:
        populateClubProfile()
        domain.profileId = INVALID_PROFILE_ID

        expect:
        !domain.validate()
    }

    void "Test clubProfile toString()"() {
        given:
        populateClubProfile()

        expect:
        domain.toString() == FIRST_NAME
    }

    void "Test clubProfile toString() with last name"() {
        given:
        populateClubProfile()
        domain.lastName = LAST_NAME

        expect:
        domain.toString() == "${FIRST_NAME} ${LAST_NAME}"
    }

    void "Test clubProfile toString() with nick name"() {
        given:
        populateClubProfile()
        domain.nickName = NICK_NAME

        expect:
        domain.toString() == "${FIRST_NAME} ${NICK_NAME}"
    }

    void "Test clubProfile toString() with last and nick names"() {
        given:
        populateClubProfile()
        domain.lastName = LAST_NAME
        domain.nickName = NICK_NAME

        expect:
        domain.toString() == "${FIRST_NAME} ${LAST_NAME} ${NICK_NAME}"
    }

    private void populateUserProfile() {
        domain.firstName = FIRST_NAME
        domain.user = user
        domain.type = ProfileType.USER
    }

    private void populateClubProfile() {
        domain.firstName = FIRST_NAME
        domain.user = user
        domain.type = ProfileType.CLUB
        domain.period = Period.ANTIQUITY
    }
}
