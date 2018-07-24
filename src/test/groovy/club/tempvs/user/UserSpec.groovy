package club.tempvs.user

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import static club.tempvs.tests.utils.TestingUtils.EMAIL
import static club.tempvs.tests.utils.TestingUtils.PASSWORD

class UserSpec extends Specification implements DomainUnitTest<User> {

    def profile = Mock Profile

    def setup() {
        domain.email = EMAIL
        domain.password = PASSWORD
        domain.userProfile = userProfile
    }

    def cleanup() {
    }

    void "User without password has not been created"() {
        given:
        domain.password = null

        expect:
        !domain.validate()
    }

    void "User without email has not been created"() {
        given:
        domain.email = null

        expect:
        !domain.validate()
    }

    void "User created"() {
        expect:
        domain.validate()
    }
}
