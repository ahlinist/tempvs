package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class ProfileViewSpec extends Specification {

    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'
    private static final String LAST_ACTIVE = 'lastActive'
    private static final String USER = 'user'
    private static final String USER_PROFILE = 'userProfile'
    private static final String CLUB_PROFILES = 'clubProfiles'

    Date lastActiveDate = new Date()

    def userProfile = Mock(UserProfile)
    def clubProfile = Mock(ClubProfile)

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
        Map model = [profile: userProfile, id: 1]
        String title = "<title>Tempvs - <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        String clubProfileLink = '<a href="/profile/clubProfile" class="list-group-item">'
        String clubProfileTag = "<tempvs:fullName profile=\"${clubProfile}\"/>"

        when:
        String result = render view: '/profile/userProfile', model: model

        then:
        result.contains title
        result.contains lastActive
        result.contains clubProfileLink
        result.contains clubProfileTag
    }

    void "Test /profile/clubProfile view"() {
        given:
        String title = "<title>Tempvs - <tempvs:fullName profile=\"${clubProfile}\"/></title>"
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        Map model = [profile: clubProfile, id: 1, sec: [username:{}]]

        when:
        String result = render view: '/profile/clubProfile', model: model

        then:
        result.contains title
        result.contains lastActive
    }

    void "Test /profile/editUserProfile view"() {
        given:
        String title = "<title>Tempvs - Edit <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String updateAvatar = '<tempvs:ajaxForm controller="profile" action="updateAvatar">'
        String updateProfileEmail = '<tempvs:ajaxForm action="updateProfileEmail">'
        String updateUserProfile = '<tempvs:ajaxForm action="updateUserProfile">'

        Map model = [profile: userProfile]

        when:
        String result = render view: '/profile/editUserProfile', model: model

        then:
        result.contains title
        result.contains updateAvatar
        result.contains updateProfileEmail
        result.contains updateUserProfile
    }

    void "Test /profile/editClubProfile view"() {
        given:
        String title = "<title>Tempvs - Edit <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String updateAvatar = '<tempvs:ajaxForm controller="profile" action="updateAvatar">'
        String updateProfileEmail = '<tempvs:ajaxForm action="updateProfileEmail">'
        String updateUserProfile = '<tempvs:ajaxForm action="updateClubProfile">'
        Map model = [profile: userProfile]

        when:
        String result = render view: '/profile/editClubProfile', model: model

        then:
        result.contains title
        result.contains updateAvatar
        result.contains updateProfileEmail
        result.contains updateUserProfile
    }

    void "Test /profile/create view"() {
        given:
        String title = "<title>Tempvs - Create profile</title>"
        String createForm = '<tempvs:ajaxForm action="create">'

        when:
        String result = render view: '/profile/create', model: model

        then:
        result.contains title
        result.contains createForm
    }

    void "Test /profile/list view"() {
        given:
        String title = "<title>Tempvs - <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String createButton = '<a href="/profile/create" class="btn btn-default">'
        String clubProfileLink = '<a href="/profile/clubProfile" class="list-group-item">'
        String clubProfileTag = "<tempvs:fullName profile=\"${clubProfile}\"/>"
        Map model = [user: user]

        when:
        String result = render view: '/profile/list', model: model

        then:
        result.contains title
        result.contains createButton
        result.contains clubProfileLink
        result.contains clubProfileTag
    }
}
