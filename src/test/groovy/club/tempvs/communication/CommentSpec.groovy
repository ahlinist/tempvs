package club.tempvs.communication

import club.tempvs.user.ClubProfile
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CommentSpec extends Specification implements DomainUnitTest<Comment> {

    private static final String TEXT = 'text'

    def clubProfile = Mock ClubProfile

    def setup() {
    }

    def cleanup() {
    }

    void "Test Comment creation"() {
        expect:
        !domain.validate()

        when:
        domain.clubProfile = clubProfile

        then:
        !domain.validate()

        when:
        domain.text = TEXT

        then:
        domain.validate()
    }
}
