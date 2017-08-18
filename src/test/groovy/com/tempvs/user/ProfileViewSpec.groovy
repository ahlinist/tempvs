package com.tempvs.user

import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class ProfileViewSpec extends Specification {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String USER = 'user'
    private static final String CLASS = 'class'
    private static final String PERIOD = 'period'
    private static final String AVATAR = 'avatar'
    private static final String LOCATION = 'location'
    private static final String CLUB_NAME = 'clubName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'
    private static final String FIRST_NAME = 'firstName'
    private static final String PROFILE_ID = 'profileId'
    private static final String LAST_ACTIVE = 'lastActive'
    private static final String USER_PROFILE = 'userProfile'
    private static final String CLUB_PROFILES = 'clubProfiles'
    private static final String PROFILE_EMAIL = 'profileEmail'


    Date lastActiveDate = new Date()
    def period = Period.valueOf'ANCIENT'

    def image = Mock(Image)
    def clubProfile = Mock(ClubProfile)
    def userProfile = Mock(UserProfile)

    def user = Mock(User) {
        getProperty(LAST_ACTIVE) >> lastActiveDate
        getProperty(USER_PROFILE) >> userProfile
        getProperty(CLUB_PROFILES) >> [clubProfile]
    }

    def setup() {
        userProfile = Mock(UserProfile) {
            getProperty(FIRST_NAME) >> FIRST_NAME
            getProperty(LAST_NAME) >> LAST_NAME
            getProperty(USER) >> user
        }

        clubProfile = Mock(ClubProfile) {
            getProperty(FIRST_NAME) >> FIRST_NAME
            getProperty(LAST_NAME) >> LAST_NAME
            getProperty(NICK_NAME) >> NICK_NAME
            getProperty(USER) >> user
        }
    }

    def cleanup() {

    }

    void "Test /profile/userProfile view"() {
        given:
        String avatar = '<tempvs:modalImage image="'
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        String updateProfileButton = '<tempvs:modalButton id="updateProfile" classes="glyphicon glyphicon-pencil">'
        String clubProfileLink = '<a href="/profile/clubProfile/id" class="list-group-item">'
        Map model = [profile: userProfile, id: 1, editAllowed: Boolean.TRUE]

        when:
        String result = render view: '/profile/userProfile', model: model

        then:
        1 * userProfile.getProperty(USER) >> user
        1 * userProfile.getProperty(AVATAR) >> image
        2 * userProfile.getProperty(CLASS) >> UserProfile
        2 * userProfile.getProperty(PROFILE_EMAIL) >> PROFILE_EMAIL
        2 * userProfile.getProperty(LOCATION) >> LOCATION
        1 * userProfile.getProperty(PROFILE_ID) >> PROFILE_ID
        1 * user.getProperty(LAST_ACTIVE) >> lastActiveDate
        1 * userProfile.getProperty(FIRST_NAME) >> FIRST_NAME
        1 * userProfile.getProperty(LAST_NAME) >> LAST_NAME
        2 * userProfile.getProperty(ID) >> ID
        1 * user.getProperty(CLUB_PROFILES) >> [clubProfile]
        1 * clubProfile.getProperty(ID) >> ID
        0 * _

        and:
        result.contains avatar
        result.contains lastActive
        result.contains clubProfileLink
        result.contains updateProfileButton
    }

    void "Test /profile/clubProfile view"() {
        given:
        String avatar = '<tempvs:modalImage image="'
        String updateProfileButton = '<tempvs:modalButton id="updateProfile" classes="glyphicon glyphicon-pencil">'
        String deleteProfileButton = '<tempvs:modalButton id="deleteProfile" size="modal-sm" classes="glyphicon glyphicon-trash">'
        Map model = [profile: clubProfile, id: 1, editAllowed: Boolean.TRUE]

        when:
        String result = render view: '/profile/clubProfile', model: model

        then:
        1 * clubProfile.getProperty(USER) >> user
        1 * clubProfile.getProperty(AVATAR) >> image
        2 * clubProfile.getProperty(CLASS) >> ClubProfile
        2 * clubProfile.getProperty(PROFILE_EMAIL) >> PROFILE_EMAIL
        2 * clubProfile.getProperty(LOCATION) >> LOCATION
        2 * clubProfile.getProperty(CLUB_NAME) >> CLUB_NAME
        1 * clubProfile.getProperty(PROFILE_ID) >> PROFILE_ID
        1 * clubProfile.getProperty(FIRST_NAME) >> FIRST_NAME
        1 * clubProfile.getProperty(LAST_NAME) >> LAST_NAME
        1 * clubProfile.getProperty(NICK_NAME) >> NICK_NAME
        2 * clubProfile.getProperty(PERIOD) >> period
        3 * clubProfile.getProperty(ID) >> ID
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(ID) >> ID
        0 * _

        and:
        result.contains avatar
        result.contains updateProfileButton
        result.contains deleteProfileButton
    }

    void "Test /profile/list view"() {
        given:
        String createButton = '<tempvs:modalButton id="createProfile" message="clubProfile.create.link">'
        String clubProfileLink = '<a href="/profile/clubProfile" class="list-group-item">'
        Map model = [userProfile: userProfile, clubProfiles: [clubProfile]]

        when:
        String result = render view: '/profile/list', model: model

        then:
        result.contains createButton
        result.contains clubProfileLink
    }
}
