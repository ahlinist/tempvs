package com.tempvs.tests

import grails.test.mixin.integration.Integration
import grails.transaction.*

import spock.lang.*
import geb.spock.*

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class UserSpec extends GebSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "register to tempvs"() {
        when:"user logged in"
        register "testUser@gmail.com", "passW0rd!", 'Test', 'User'

        then:"user's name is present on show page"
        $('#test').text() == 'test'
    }

/*    void "login to tempvs"() {
        when:"user logged in"
            login "anton.hlinisty@gmail.com", "passW0rd!"

        then:"user's name is present on show page"
        	$('#fullname').text() == 'Anton Hlinisty'
    }*/

    private register(String email, String password, String firstName, String lastName) {
        go "/"
        $('#registerTab').click()
        $('input[name=email]').value(email)
        $('input[name=password]').value(password)
        $('input[name=repeatPassword]').value(password)
        $('input[name=firstName]').value(firstName)
        $('input[name=lastName]').value(lastName)
        $('input[name=register]').click()
    }

/*    private login(String email, String password) {
        go "/"
        $('input[name=email]').value(email)
        $('input[name=password]').value(password)
        $('input[name=login]').click()
    }*/
}
