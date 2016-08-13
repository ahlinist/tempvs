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
class UserProfileFunctionalSpec extends GebSpec {
    String EMAIL = 'userProfileFunctionalTest@gmail.com'
    String EMAIL_STATIC= 'userProfileFunctionalTest2@gmail.com'
    String PASSWORD = 'passW0rd!'
    String FIRST_NAME = 'testFirstName'
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

    void "register static user"() {
        when: "register user"
        register EMAIL_STATIC, PASSWORD, FIRST_NAME

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
        register EMAIL, PASSWORD, FIRST_NAME

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

    private fillFields(Map fieldMap){
        fieldMap.each { key, value ->
            $("input[name=${key}]").value(value)
        }

        $("input[name=updateUserProfile]").click()
    }

    private gotoUserProfile() {
        go '/userProfile'
    }
}
