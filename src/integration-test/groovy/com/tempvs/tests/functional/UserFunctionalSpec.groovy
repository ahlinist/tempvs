package com.tempvs.tests.functional

import grails.test.mixin.integration.Integration
import grails.transaction.*

import geb.navigator.EmptyNavigator
import geb.spock.*
import spock.lang.Shared

/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class UserFunctionalSpec extends GebSpec {
    String EMAIL = 'userFunctionalTest@gmail.com'
    String EMAIL_FOR_FAIL = 'fakeEmail'
    String NEW_EMAIL = 'newUserFunctionalTest@gmail.com'
    String PASSWORD = 'passW0rd!'
    String INCORRECT_PASSWORD = 'incorrectPassword'
    String NEW_PASSWORD = 'newPassw0rd!'
    String FIRST_NAME = 'First name'
    String LAST_NAME = 'Last name'

    String EMAIL_STATIC= 'userProfileFunctionalTest2@gmail.com'

    @Shared
    Map fieldMap = [firstName: 'testFirstName2',
                    lastName: 'testLastName',
                    profileEmail: 'userProfileFunctionalTest@gmail.com',
                    location: 'testLocation',
                    customId: 'testCustomId']
    @Shared
    Map fieldMapStatic = [firstName: 'testFirstNameStatic',
                          lastName: 'testLastName',
                          profileEmail: 'userProfileFunctionalTestStatic@gmail.com',
                          location: 'testLocation',
                          customId: 'testCustomIdStatic']

    def setup() {
        login EMAIL, PASSWORD
        gotoUserProfile()
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
        when:"user registers"
        register EMAIL, PASSWORD, FIRST_NAME, LAST_NAME

        then:"user's name is present on show page"
        $('div').find{it.text().contains(FIRST_NAME)}
    }

    void "login to tempvs"() {
        when: "logoff"
        logoff()

        and:"user logged in"
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

    void "register static user"() {
        when: "register user"
        register EMAIL_STATIC, PASSWORD, FIRST_NAME, LAST_NAME

        then: "find first name"
        $('div').find {it.text() == FIRST_NAME}
    }

    void "set static user's profile fields"() {
        when: "login as static"
        login EMAIL_STATIC, PASSWORD

        and: "set fields"
        fillFields(fieldMapStatic)

        then:
        $("input[name=${fieldName}]").value() == value

        where:
        fieldName            | value
        'firstName'          | fieldMapStatic.firstName
        'lastName'           | fieldMapStatic.lastName
        'profileEmail'       | fieldMapStatic.profileEmail
        'customId'           | fieldMapStatic.customId
        'location'           | fieldMapStatic.location
    }

    void "register user"() {
        when: "register user"
        register EMAIL, PASSWORD, FIRST_NAME, LAST_NAME

        then: "find first name"
        $('div').find {it.text() == FIRST_NAME}
    }

    void "find no last name"() {
        expect:"find no last name"
        !$("input[name=${fieldName}]").value()

        where:
        fieldName            | _
        'lastName'           | _
        'profileEmail'       | _
        'customId'           | _
        'location'           | _
    }

    void "set user's profile fields"() {
        when: "set fields"
        fillFields(fieldMap)

        then:
        $("input[name=${fieldName}]").value() == value

        where:
        fieldName            | value
        'firstName'          | fieldMap.firstName
        'lastName'           | fieldMap.lastName
        'profileEmail'       | fieldMap.profileEmail
        'customId'           | fieldMap.customId
        'location'           | fieldMap.location
    }

    void "set customId containing numbers only"() {
        when: "customId containing only numbers is set"
        fillFields([customId: 123])

        then: "it is not saved and alert is shown"
        $('div.alert-danger')
    }

    void "set non-unique email"() {
        when: "email of static user is set"
        fillFields(profileEmail: fieldMapStatic.profileEmail)

        then: "it is not saved and alert is shown"
        $('div.alert-danger')
    }

    private register(String email, String password, String firstName, String lastName) {
        go '/user/register'
        $('input[name=email]').value(email)
        $('input[name=password]').value(password)
        $('input[name=repeatPassword]').value(password)
        $('input[name=firstName]').value(firstName)
        $('input[name=lastName]').value(lastName)
        $('input[name=register]').click()
    }

    private login(String email, String password) {
        go '/user/login'
        $('input[name=username]').value(email)
        $('input[name=password]').value(password)
        $('input[name=login]').click()
    }

    private fillFields(Map fieldMap){
        fieldMap.each { key, value ->
            $("input[name=${key}]").value(value)
        }

        $("input[name=updateUserProfile]").click()
    }

    private gotoUserProfile() {
        go '/userProfile'
    }

    private logoff() {
        go "/logoff"
    }

    private changePassword(String currentPassword, String newPassword, String repeatNewPassword) {
        go '/user/edit'
        $('input[name=currentPassword]').value(currentPassword)
        $('input[name=newPassword]').value(newPassword)
        $('input[name=repeatNewPassword]').value(repeatNewPassword)
        $('input[name=updatePassword]').click()
    }

    private changeEmail(String email) {
        go '/user/edit'
        $('input[name=email]').value(email)
        $('input[name=updateEmail]').click()
    }

    private Boolean empty(selector) {
        selector instanceof EmptyNavigator
    }
}
