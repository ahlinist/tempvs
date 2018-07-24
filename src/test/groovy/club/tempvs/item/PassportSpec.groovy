package club.tempvs.item

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PassportSpec extends Specification implements DomainUnitTest<Passport> {

    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def item = Mock Item
    def clubProfile = Mock ClubProfile

    def setup() {
    }

    def cleanup() {
    }

    void "Test Passport creation"() {
        expect:
        !new Passport().validate()

        and:
        !new Passport(clubProfile: clubProfile).validate()

        and:
        !new Passport(clubProfile: clubProfile, description: DESCRIPTION).validate()

        and:
        new Passport(clubProfile: clubProfile, description: DESCRIPTION, name: NAME).validate()
    }
}
