package com.tempvs.tests.integration

import com.tempvs.domain.user.User
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserProfileIntegrationSpec extends Specification {
    def userService
    String EMAIL = 'userProfileIntegrationTest@mail.com'
    String PASSWORD = 'passW0rd'
    String FIRST_NAME = 'Test_first_name'

    def setup() {
    }

    def cleanup() {
    }

    void "Created user has userProfile"() {
        when: "Create user"
        userService.createUser(email: EMAIL, password: PASSWORD, firstName: FIRST_NAME)

        then: "Created user has userProfile"
        User.findByEmail(EMAIL).userProfile
    }
}
