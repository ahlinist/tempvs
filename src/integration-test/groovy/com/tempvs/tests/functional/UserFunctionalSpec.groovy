package com.tempvs.tests.functional

import grails.test.mixin.integration.Integration
import grails.transaction.*

import geb.spock.*
import com.tempvs.tests.functional.pages.LoginPage
import com.tempvs.tests.functional.pages.RegisterPage

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class UserFunctionalSpec extends GebSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "Login page is available for non-logged users"() {
        when: 'Browser is pointed to /auth/login'
        to LoginPage

        then: 'Login page is rendered'
        at LoginPage
    }

    void "Register page is available for non-logged users"() {
        when: 'Browser is pointed to /auth/register'
        to RegisterPage

        then: 'Register page is rendered'
        at RegisterPage
    }

    void "Home page redirects to login if not logged in"() {
        when: "The home page is visited"
        go '/'

        then: "Login page is rendered"
        at LoginPage
    }
}
