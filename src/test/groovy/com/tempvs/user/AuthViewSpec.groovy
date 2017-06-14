package com.tempvs.user

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class AuthViewSpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "Test /auth/index view"() {
        given:
        String title = '<title>Tempvs - Auth</title>'
        String loginForm = '<tempvs:ajaxForm controller="auth" action="login">'
        String registerForm = '<tempvs:ajaxForm controller="auth" action="register">'

        when:
        String result = render view: '/auth/index'

        then:
        result.contains title
        result.contains loginForm
        result.contains registerForm
    }
}
