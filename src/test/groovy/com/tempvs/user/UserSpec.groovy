package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification

import static com.tempvs.tests.utils.TestingUtils.DEFAULT_USER_PROPS
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    private static final String INVALID_EMAIL = 'test-email.com'

    def userProfile = Mock(UserProfile)

    def userService = Mock(UserService) {
        isEmailUnique(_ as String) >> Boolean.TRUE
    }

    User user

    def setup() {
        Map params = DEFAULT_USER_PROPS.clone()
        user = new User(params + [userService: userService, userProfile: userProfile])
    }

    def cleanup() {
    }

    void "User without password has not been created"() {
        given:
        user.password = null

        expect:
        !user.validate()
    }

    void "User without email has not been created"() {
        given:
        user.email = null

        expect:
        !user.validate()
    }

    void "User created"() {
        expect:
        user.validate()
    }

    void "Users with only unique emails are saved"() {
        given:
        def userService = Mock(UserService) {
            isEmailUnique(_ as String) >> Boolean.FALSE
        }

        user.userService = userService

        expect:
        !user.validate()
    }
}
