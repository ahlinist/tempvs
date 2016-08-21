package com.tempvs.tests.integration

import com.tempvs.domain.user.User
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserProfileIntegrationSpec extends Specification {
    def userService
    def userProfileService
    def springSecurityService
    String EMAIL = 'userProfileIntegrationTest@mail.com'
    String NON_UNIQUE_EMAIL = 'userProfileIntegrationTestNonUnique@mail.com'
    String PASSWORD = 'passW0rd'
    String FIRST_NAME = 'Test_first_name'
    String CUSTOM_ID = 'test.Custom-Id_12'
    String NUMERIC_CUSTOM_ID = '123456'
    @Shared
    Map fieldMap = [firstName: 'TestFirstName2', lastName: 'TestLastName', profileEmail: EMAIL,
                    location: 'testLocation', customId: CUSTOM_ID]

    def setup() {
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME)
        springSecurityService.reauthenticate(EMAIL, PASSWORD)
    }

    def cleanup() {
    }

    void "Created user has userProfile"() {
        expect: "Created user has userProfile"
        User.findByEmail(EMAIL).userProfile.firstName == FIRST_NAME
    }

    void "Update user profile fields"() {
        when: "Assign values"
        userProfileService.updateUserProfile(fieldMap)

        then: "Values are saved in DB"
        User.findByEmail(EMAIL).userProfile."${fieldName}" == fieldValue

        where:
        fieldName            | fieldValue
        'firstName'          | fieldMap.firstName
        'lastName'           | fieldMap.lastName
        'profileEmail'       | fieldMap.profileEmail
        'customId'           | fieldMap.customId
        'location'           | fieldMap.location
    }

    void "Attempt to set non-unique profileEmail"() {
        when: "Create additional user"
        userService.createUser(email: NON_UNIQUE_EMAIL, password: PASSWORD, firstName: FIRST_NAME)

        and: "Set additional user's login email as user's profileEmail"
        userProfileService.updateUserProfile([profileEmail: NON_UNIQUE_EMAIL])

        then: "Check that non-unique profileEmail was not assigned"
        User.findByEmail(EMAIL).userProfile.profileEmail != NON_UNIQUE_EMAIL
    }

    void "Attempt to set numeric customId"() {
        when: 'set numeric customId'
        userProfileService.updateUserProfile([customId: NUMERIC_CUSTOM_ID])

        then: 'numeric customId is not saved, old value is used instead'
        User.findByEmail(EMAIL).userProfile.customId == CUSTOM_ID
    }
}
