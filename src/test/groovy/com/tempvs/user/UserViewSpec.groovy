package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class UserViewSpec extends Specification {

    private static final String EMAIL_ADDRESS = 'test@email.com'

    def user = Mock(User) {
        getEmail() >> EMAIL_ADDRESS
    }

    def setup() {

    }

    def cleanup() {

    }

    void "Test /user/edit view"() {
        given:
        String title = "<title>Tempvs - Edit ${EMAIL_ADDRESS}</title>"
        String updateEmail = '<tempvs:ajaxForm action="updateEmail">'
        String updatePassword = '<tempvs:ajaxForm action="updatePassword">'
        Map model = [user: user]

        when:
        String result = render view: '/user/edit', model: model

        then:
        result.contains title
        result.contains updateEmail
        result.contains updatePassword
    }
}
