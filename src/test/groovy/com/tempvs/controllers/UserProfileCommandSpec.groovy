package com.tempvs.controllers

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
class UserProfileCommandSpec extends Specification {
    public static final String FIRST_NAME = 'firstName'
    public static final String LAST_NAME = 'lastName'
    public static final String LOCATION = 'location'
    public static final String CUSTOM_ID = 'customId'
    public static final String NUMERIC_CUSTOM_ID = '1234'

    def setup() {

    }

    def cleanup() {

    }

    void "Create empty UserProfileCommand"() {
        expect:
        !new UserProfileCommand().validate()
    }

    void "Create UserProfileCommand with first and last name"() {
        expect:
        new UserProfileCommand(firstName: FIRST_NAME, lastName: LAST_NAME).validate()
    }

    void "Create full UserProfileCommand"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, location: LOCATION, customId: CUSTOM_ID]

        expect:
        new UserProfileCommand(props).validate()
    }

    void "Create UserProfileCommand with numeric customId"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, customId: NUMERIC_CUSTOM_ID]

        expect:
        !new UserProfileCommand(props).validate()
    }
}
