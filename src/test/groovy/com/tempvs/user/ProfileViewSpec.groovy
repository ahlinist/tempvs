package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class ProfileViewSpec extends Specification {

    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'

    Date lastActiveDate = new Date()

    def user = Mock(User) {
        getLastActive() >> lastActiveDate
    }

    def userProfile = Mock(UserProfile) {
        getFirstName() >> FIRST_NAME
        getLastName() >> LAST_NAME
        getUser() >> user
    }

    def clubProfile = Mock(ClubProfile) {
        getFirstName() >> FIRST_NAME
        getLastName() >> LAST_NAME
        getNickName() >> NICK_NAME
        getUser() >> user
    }

    def setup() {

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
        user.clubProfiles >> [clubProfile]

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
        String title = "<title>Tempvs - <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        Map model = [profile: userProfile, id: 1]

        when:
        String result = render view: '/profile/clubProfile', model: model

        then:
        result.contains title
        result.contains lastActive
    }

    void "Test /profile/editUserProfile view"() {
        given:
        String title = "<title>Tempvs - Edit <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String updateAvatar = '<tempvs:ajaxForm controller="image" action="updateAvatar">'
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
        String updateAvatar = '<tempvs:ajaxForm controller="image" action="updateAvatar">'
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
        user.clubProfiles >> [clubProfile]
        user.userProfile >> userProfile

        when:
        String result = render view: '/profile/list', model: model

        then:
        result.contains title
        result.contains createButton
        result.contains clubProfileLink
        result.contains clubProfileTag
    }
}
