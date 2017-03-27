package com.tempvs.user.verification

import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestMixin(GroovyPageUnitTestMixin)
class VerifyViewSpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "Test /user/verify"() {
        given:
        String title = '<title>Tempvs - Email verification</title>'

        expect:
        render(view: '/user/verify').contains title
    }
}
