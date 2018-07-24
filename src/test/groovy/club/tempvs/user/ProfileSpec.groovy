package club.tempvs.user

import club.tempvs.item.Passport
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import static club.tempvs.tests.utils.TestingUtils.FIRST_NAME
import static club.tempvs.tests.utils.TestingUtils.LAST_NAME
import static club.tempvs.tests.utils.TestingUtils.NICK_NAME

class ProfileSpec extends Specification implements DomainUnitTest<Profile> {

    private static final String NOT_EMAIL = 'not email'
    private static final String NON_VALID_EMAIL = 'non-valid-email'
    private static final String NUMERIC_PROFILE_ID = '123456'

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

    void "Test fails when firstName is missing for UserProfile"() {
        given:
        domain.firstName = null
        domain.type = ProfileType.USER

        expect:
        !domain.validate()
    }

    void "Test fails when lastName is missing"() {
        given:
        userProfile.lastName = null

        expect:
        !userProfile.validate()
    }

    void "User with incorrect email is not created"() {
        given:
        userProfile.profileEmail = NOT_EMAIL

        expect:
        !userProfile.validate()
    }

    void "User can not be created with numeric profileId"() {
        given:
        userProfile.profileId = NUMERIC_PROFILE_ID

        expect:
        !userProfile.validate()
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

    void "User  can not be created with numeric profileId"() {
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
