package com.tempvs.user

import spock.lang.Specification
/**
 * {@link com.tempvs.user.ProfileHolder}-related unit-test suite.
 */
class ProfileHolderSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String CLASS = 'class'
    private static final String USER_PROFILE = 'userProfile'
    private static final String CLUB_PROFILES = 'clubProfiles'

    def user = Mock User
    Class clazz = Object.class
    def userService = Mock UserService
    def baseProfile = Mock BaseProfile
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def profileService = Mock ProfileService

    ProfileHolder profileHolder

    def setup() {
        profileHolder = new ProfileHolder()
        profileHolder.userService = userService
        profileHolder.profileService = profileService
    }

    def cleanup() {

    }

    void "Test getProfile() when not logged in"() {
        when:
        def result = profileHolder.getProfile()

        then:
        1 * userService.currentUser >> null
        0 * _

        and:
        result == null
    }

    void "Test getProfile() when logged in and profile info not populated."() {
        given:
        profileHolder.clazz == null
        profileHolder.id == null

        when:
        def result = profileHolder.getProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * userProfile.getProperty(ID) >> LONG_ID
        0 * _

        and:
        result == userProfile
    }

    void "Test getProfile() when logged in and profile info populated. Profile missing in DB."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = LONG_ID

        when:
        def result = profileHolder.getProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * profileService.getProfile(clazz, LONG_ID) >> null
        0 * _

        and:
        result == null
    }

    void "Test getProfile() when logged in and profile info populated. Profile present in DB and belongs to user."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = LONG_ID

        when:
        def result = profileHolder.getProfile()

        then:
        1 * userService.currentUser >> user
        1 * profileService.getProfile(clazz, LONG_ID) >> baseProfile
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * baseProfile.equals(userProfile)  >> Boolean.TRUE
        0 * _

        and:
        result == baseProfile
    }

    void "Test getProfile() when logged in and profile info populated. Profile present in DB and doesn't belong to user."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = LONG_ID

        when:
        def result = profileHolder.getProfile()

        then:
        1 * userService.currentUser >> user
        1 * profileService.getProfile(clazz, LONG_ID) >> baseProfile
        1 * user.getProperty(CLUB_PROFILES) >> [clubProfile]
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * baseProfile.equals(userProfile)  >> Boolean.FALSE
        1 * userProfile.getProperty(ID) >> LONG_ID
        0 * _

        and:
        result == userProfile
    }

    void "Test setProfile()"() {
        when:
        profileHolder.setProfile(userProfile)

        then:
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * userProfile.getProperty(ID) >> 1L
        0 * _

        and:
        profileHolder.id == 1L
    }
}
