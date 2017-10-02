package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class UserViewSpec extends Specification {

    private static final String ID = 'id'
    private static final String EMAIL = 'email'
    private static final String EMAIL_ADDRESS = 'test@email.com'

    def user = Mock User
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile

    def setup() {

    }

    def cleanup() {

    }

    void "Test /user/edit view"() {
        given:
        String title = "<title>Tempvs - Edit ${EMAIL_ADDRESS}</title>"
        String updateEmail = '<tempvs:ajaxSmartForm type="text" action="updateEmail"'
        String updatePassword = '<tempvs:ajaxForm action="updatePassword">'
        Map model = [user: user, userProfile: userProfile, clubProfiles: [clubProfile]]

        when:
        String result = render view: '/user/edit', model: model

        then:
        2 * user.getProperty(EMAIL) >> EMAIL_ADDRESS
        1 * userProfile.getProperty(ID) >> ID
        1 * clubProfile.getProperty(ID) >> ID
        0 * _

        and:
        result.contains title
        result.contains updateEmail
        result.contains updatePassword
    }
}
