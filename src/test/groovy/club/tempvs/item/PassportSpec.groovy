package club.tempvs.item

import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PassportSpec extends Specification implements DomainUnitTest<Passport> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def item = Mock Item

    def clubProfile = Mock(Profile) {
        getType() >> ProfileType.CLUB
    }

    def setup() {
    }

    def cleanup() {
    }

    void "Test Passport creation with insufficient data"() {
        expect:
        !new Passport().validate()

        and:
        !new Passport(profile: clubProfile).validate()

        and:
        !new Passport(profile: clubProfile, description: DESCRIPTION).validate()
    }

    void "Test Passport creation for userProfile"() {
        given:
        def userProfile = Mock(Profile) {
            getType() >> ProfileType.USER
        }

        expect:
        !new Passport(profile: userProfile, description: DESCRIPTION, name: NAME).validate()
    }

    void "Test successful Passport creation"() {
        expect:
        new Passport(profile: clubProfile, description: DESCRIPTION, name: NAME).validate()
    }
}
