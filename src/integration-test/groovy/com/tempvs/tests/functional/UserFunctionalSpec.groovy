package com.tempvs.tests.functional

import grails.test.mixin.integration.Integration
import grails.transaction.*

import geb.navigator.EmptyNavigator
import geb.spock.*

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class UserFunctionalSpec extends GebSpec {
    String EMAIL = 'testUser@gmail.com'
    String EMAIL_FOR_FAIL = 'fakeEmail'
    String NEW_EMAIL = 'newTestUser@gmail.com'
    String PASSWORD = 'passW0rd!'
    String INCORRECT_PASSWORD = 'incorrectPassword'
    String NEW_PASSWORD = 'newPassw0rd!'
    String FIRST_NAME = 'First name'
    String LAST_NAME = 'Last name'


    def setup() {
    }

    def cleanup() {
    }

    void "register with non valid email"() {
        when:"attempt to register"
        register EMAIL_FOR_FAIL, PASSWORD, FIRST_NAME, LAST_NAME

        then:"returned back to register page with alert msg"
        ($('form[action="/user/register"]') && $('div.alert-danger'))
    }

    void "login as non-existant user"() {
        when:"logging in"
        login EMAIL_FOR_FAIL, PASSWORD

        then:"return back to login page"
        $('form[action="/login/authenticate"]')
    }

    void "register to tempvs"() {
        when:"user logged in"
        register EMAIL, PASSWORD, FIRST_NAME

        then:"user's name is present on show page"
        $('div').find{it.text().contains(FIRST_NAME)}
    }

    void "login to tempvs"() {
        when:"user logged in"
        login EMAIL, PASSWORD

        then:"user's name is present on show page"
        $('div').find{it.text().contains(FIRST_NAME)}
    }

    void "log off"() {
        when:"user logged in"
        login EMAIL, PASSWORD

        and:"go to logoff url"
        logoff()

        then:"redirected to login page"
        $('form[action="/login/authenticate"]')
    }

    void "change password entering incorrect current"() {
        when:"user logged in"
        login EMAIL, PASSWORD

        and:"change password entering incorrect current password"
        changePassword INCORRECT_PASSWORD, NEW_PASSWORD, NEW_PASSWORD

        then:"get alert msg"
        ($('form[action="/user/updatePassword"]') && $('div.alert-danger'))
    }

    void "change password entering incorrect repeated"() {
        when:"user logged in"
        login EMAIL, PASSWORD

        and:"enter incorrect repeat password"
        changePassword PASSWORD, NEW_PASSWORD, INCORRECT_PASSWORD

        then:"get alert msg"
        ($('form[action="/user/updatePassword"]') && $('div.alert-danger'))
    }

    void "change password and log in with new"() {
        when:"user logged in"
        login EMAIL, PASSWORD

        and:"change password"
        changePassword PASSWORD, NEW_PASSWORD, NEW_PASSWORD

        and:"log off"
        logoff()

        and:"log in with new password"
        login EMAIL, NEW_PASSWORD

        then:"user's name is present on show page"
        $('div').findAll{it.text().contains(FIRST_NAME)}
    }

    void "change email entering incorrect one"() {
        when:"user logged in"
        login EMAIL, NEW_PASSWORD

        and:"enter incorrect email"
        changeEmail EMAIL_FOR_FAIL

        then:"no success msg"
        !empty($('form[action="/user/updateEmail"]')) && empty($('div.alert-success'))
    }

    void "change email and login with new"() {
        when:"user logged in"
        login EMAIL, NEW_PASSWORD

        and:"enter correct email"
        changeEmail NEW_EMAIL

        and:"log off"
        logoff()

        and:"log in with new password"
        login NEW_EMAIL, NEW_PASSWORD

        then:"user's name is present on show page"
        $('div').findAll{it.text().contains(FIRST_NAME)}
    }

    private register(String email, String password, String firstName, String lastName = null) {
        go '/user/register'
        $('input[name=email]').value(email)
        $('input[name=password]').value(password)
        $('input[name=repeatPassword]').value(password)
        $('input[name=firstName]').value(firstName)
        $('input[name=lastName]').value(lastName ?: '')
        $('input[name=register]').click()
    }

    private login(String email, String password) {
        go '/user/login'
        $('input[name=username]').value(email)
        $('input[name=password]').value(password)
        $('input[name=login]').click()
    }

    private logoff() {
        go "/logoff"
    }

    private changePassword(String currentPassword, String newPassword, String repeatNewPassword) {
        go '/user/editUser'
        $('input[name=currentPassword]').value(currentPassword)
        $('input[name=newPassword]').value(newPassword)
        $('input[name=repeatNewPassword]').value(repeatNewPassword)
        $('input[name=updatePassword]').click()
    }

    private changeEmail(String email) {
        go '/user/editUser'
        $('input[name=email]').value(email)
        $('input[name=updateEmail]').click()
    }

    private Boolean empty(selector) {
        selector instanceof EmptyNavigator
    }
}
