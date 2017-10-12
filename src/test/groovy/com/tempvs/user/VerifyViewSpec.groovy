package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class VerifyViewSpec extends Specification {

    private static final String MESSAGE = 'message'

    def setup() {

    }

    def cleanup() {

    }

    void "Test /verify/registration"() {
        given:
        String title = '<title>Tempvs - Register user</title>'
        String registerForm = '<tempvs:ajaxForm controller="user" action="register">'

        when:
        String result = render view: '/verify/registration'

        then:
        result.contains title
        result.contains registerForm
    }

    void "Test /verify/error"() {
        given:
        String title = '<title>Tempvs - Email verification error</title>'
        Map model = [notFoundMessage: MESSAGE]

        when:
        String result = render view: '/verify/error', model: model

        then:
        result.contains title
        result.contains '<div class="alert alert-danger">message</div>'
    }
}
