package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class UserProfileViewSpec extends Specification {

    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String USER = 'user'
    private static final String LAST_ACTIVE = 'lastActive'

    Date lastActiveDate = new Date()

    def user = Mock(User) {
        getProperty(LAST_ACTIVE) >> lastActiveDate
    }

    def userProfile = Mock(UserProfile) {
        getProperty(FIRST_NAME) >> FIRST_NAME
        getProperty(LAST_NAME) >> LAST_NAME
        getProperty(USER) >> user
    }

    def setup() {

    }

    def cleanup() {

    }

    void "Test /userProfile/show view"() {
        given:
        String title = "<title>Tempvs - <tempvs:fullName profile=\"${userProfile}\"/></title>"
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        Map model = [profile: userProfile, id: 1]

        when:
        String result = render view: '/userProfile/show', model: model

        then:
        result.contains title
        result.contains lastActive
    }

    void "Test /userProfile/edit view"() {
        given:
        String title = "<title>Tempvs - Edit ${FIRST_NAME} ${LAST_NAME}</title>"
        String updateAvatar = '<tempvs:ajaxForm controller="image" action="updateAvatar">'
        String updateProfileEmail = '<tempvs:ajaxForm action="updateProfileEmail">'
        String updateUserProfile = '<tempvs:ajaxForm action="updateUserProfile">'
        Map model = [profile: userProfile]

        when:
        String result = render view: '/userProfile/edit', model: model

        then:
        result.contains title
        result.contains updateAvatar
        result.contains updateProfileEmail
        result.contains updateUserProfile
    }
}
