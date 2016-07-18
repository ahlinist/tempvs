package com.tempvs.domain.user

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
class UserProfileSpec extends Specification {
    String EMAIL_FOR_FAIL = 'fail@mail.com'
    String EMAIL_FOR_SUCCESS = 'success@mail.com'
    String PASSWORD = 'passW0rd'
    String FIRST_NAME = 'testFirstName'
    String MIDDLE_NAME = 'testMiddleName'
    String LAST_NAME = 'testLastName'
    String CUSTOM_ID = 'testCustomId'
    String PROFILE_EMAIL = 'profile@mail.com'
    String LOCATION = 'testLocation'

    def setup() {
        createUser(EMAIL_FOR_FAIL, PASSWORD, '', '', '', '', '', LOCATION)
        createUser(EMAIL_FOR_SUCCESS, PASSWORD, FIRST_NAME, MIDDLE_NAME,
                   LAST_NAME, CUSTOM_ID, PROFILE_EMAIL, '')
    }

    def cleanup() {
    }

    void "test fail on first name missing"() {
        expect:"userProfile creation failed"
            !UserProfile.findByLocation(LOCATION)
    }

    void "userProfile created"() {
        expect: "pass all values and userProfile is created"
            UserProfile.findByFirstName(FIRST_NAME)
    }

    private void createUser(String email, String password, String firstName, String lastName,
                            String middleName, String customId, String profileEmail, String location){
        UserProfile userProfile = new UserProfile(firstName:firstName, middleName: middleName, lastName: lastName,
                                           customId:customId, profileEmail: profileEmail, location:location)
        userProfile.user = new User(email: email, password: password, lastActive: new Date())
        userProfile.save(flush:true)
    }
}
