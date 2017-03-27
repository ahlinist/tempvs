package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class UserViewSpec extends Specification {

    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String USER_PROFILE = 'userProfile'
    private static final String LAST_ACTIVE = 'lastActive'

    def user
    def userProfile
    Date lastActiveDate = new Date()

    def setup() {
        userProfile = Mock(UserProfile) {
            getProperty(FIRST_NAME) >> FIRST_NAME
            getProperty(LAST_NAME) >> LAST_NAME
        }

        user = Mock(User) {
            getProperty(USER_PROFILE) >> userProfile
            getProperty(LAST_ACTIVE) >> lastActiveDate
        }
    }

    def cleanup() {

    }

    void "Test /user/show view"() {
        given:
        String title = "<title>Tempvs - ${FIRST_NAME} ${LAST_NAME}</title>"
        String lastActive = "<tempvs:dateFromNow date=\"${lastActiveDate}\"/>"
        Map model = [user: user, id: 1]

        when:
        String result = render view: '/user/show', model: model

        then:
        result.contains title
        result.contains lastActive
    }

    void "Test /user/register view"() {
        given:
        String title = '<title>Tempvs - Register user</title>'
        String registerForm = '<tempvs:ajaxForm action="register">'

        when:
        String result = render view: '/user/register'

        then:
        result.contains title
        result.contains registerForm
    }

    void "Test /user/edit view"() {
        given:
        String title = "<title>Tempvs - Edit ${FIRST_NAME} ${LAST_NAME}</title>"
        String updateEmail = '<tempvs:ajaxForm action="updateEmail">'
        String updatePassword = '<tempvs:ajaxForm action="updatePassword">'
        String updateAvatar = '<tempvs:ajaxForm controller="image" action="updateAvatar">'
        String updateProfileEmail = '<tempvs:ajaxForm action="updateProfileEmail">'
        String updateUserProfile = '<tempvs:ajaxForm action="updateUserProfile">'
        Map model = [user: user]

        when:
        String result = render view: '/user/edit', model: model

        then:
        result.contains title
        result.contains updateEmail
        result.contains updatePassword
        result.contains updateAvatar
        result.contains updateProfileEmail
        result.contains updateUserProfile
    }
}
