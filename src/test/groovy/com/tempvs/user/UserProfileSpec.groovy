package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification

import static com.tempvs.tests.utils.TestingUtils.DEFAULT_USER_PROFILE_PROPS

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
class UserProfileSpec extends Specification {

    private static final String NOT_EMAIL = 'not email'
    private static final String NUMERIC_PROFILE_ID = '123456'

    def user = Mock(User)

    def profileService = Mock(ProfileService) {
        isProfileEmailUnique(_ as BaseProfile, _ as String) >> Boolean.TRUE
    }

    UserProfile userProfile

    def setup() {
        Map params = DEFAULT_USER_PROFILE_PROPS.clone()
        userProfile = new UserProfile(params + [user: user, profileService: profileService])
    }

    def cleanup() {
    }

    void "Test fails when firstName is missing"() {
        given:
        userProfile.firstName = null

        expect:
        !userProfile.validate()
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

    void "Test profileEmail uniqueness"() {
        given:
        def profileService = Mock(ProfileService) {
            isProfileEmailUnique(_ as BaseProfile, _ as String) >> Boolean.FALSE
        }

        and:
        userProfile.profileService = profileService

        expect:
        !userProfile.validate()
    }
}
