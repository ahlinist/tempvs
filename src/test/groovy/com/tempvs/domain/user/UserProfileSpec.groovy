package com.tempvs.domain.user

import com.tempvs.tests.utils.TestingUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
@Mock([User, UserProfile])
class UserProfileSpec extends Specification {
    private static final String EMAIL_FOR_FAIL = 'fail@mail.com'
    private static final String NOT_EMAIL = 'not email'
    private static final String NUMERIC_CUSTOM_ID = '123456'

    def setup() {
    }

    def cleanup() {
    }

    void "Test fails when firstName is missing"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.firstName = null

        expect:
        !TestingUtils.createUser(props)
    }

    void "Test fails when lastName is missing"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.lastName = null

        expect:
        !TestingUtils.createUser(props)
    }

    void "User with incorrect email is not created"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.email = NOT_EMAIL

        expect:
        !TestingUtils.createUser(props)
    }

    void "UserProfile created"() {
        expect:
        TestingUtils.createUser().userProfile
    }

    void "User can not be created with numeric customId"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.customId = NUMERIC_CUSTOM_ID

        expect:
        !TestingUtils.createUser(props)
    }

    void "Numeric customId can't be set to existent user"() {
        given: 'An existent user'
        User user = TestingUtils.createUser()

        when: "Set numeric customId to existent user's profile"
        UserProfile userProfile = user.userProfile
        userProfile.customId = NUMERIC_CUSTOM_ID
        userProfile.save(flush:true)

        then: "UserProfile has not been saved"
        !UserProfile.findByCustomId(NUMERIC_CUSTOM_ID)
    }
    
    void "Check if email update is rejected for non-unique profileEmail"() {
        given:
        Map props = TestingUtils.DEFAULT_USER_PROPS.clone()
        props.email = EMAIL_FOR_FAIL
        props.customId = null

        expect:
        TestingUtils.createUser()
        !TestingUtils.createUser(props)
    }
}
