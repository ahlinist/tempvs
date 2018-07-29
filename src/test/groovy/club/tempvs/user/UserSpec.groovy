package club.tempvs.user

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import static club.tempvs.tests.utils.TestingUtils.EMAIL
import static club.tempvs.tests.utils.TestingUtils.PASSWORD

class UserSpec extends Specification implements DomainUnitTest<User> {


    def userProfile = Mock(Profile) {
        getType() >> ProfileType.USER
    }

    def clubProfile = Mock(Profile) {
        getType() >> ProfileType.CLUB
    }

    def setup() {

    }

    def cleanup() {
    }

    void "Test user creation with basic properties"() {
        given:
        populateUser()

        expect:
        domain.validate()
    }

    void "Test user validity without email"() {
        given:
        populateUser()
        domain.email = null

        expect:
        !domain.validate()
    }

    void "Test user validity without password"() {
        given:
        populateUser()
        domain.password = null

        expect:
        !domain.validate()
    }

    void "Test user validity without userProfile"() {
        given:
        populateUser()
        domain.profiles = []

        expect:
        !domain.validate()
    }

    void "Test user validity with clubProfile instead of userprofile"() {
        given:
        populateUser()
        domain.profiles = [clubProfile]

        expect:
        !domain.validate()
    }

    void "Test user validity with profiles of different type assigned"() {
        given:
        populateUser()
        domain.profiles = [userProfile, clubProfile]

        expect:
        domain.validate()
    }

    private void populateUser() {
        domain.email = EMAIL
        domain.password = PASSWORD
        domain.profiles = [userProfile]
    }
}
