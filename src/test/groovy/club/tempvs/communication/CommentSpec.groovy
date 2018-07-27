package club.tempvs.communication

import club.tempvs.user.Profile
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CommentSpec extends Specification implements DomainUnitTest<Comment> {

    private static final String TEXT = 'text'

    def profile = Mock Profile

    def setup() {
    }

    def cleanup() {
    }

    void "Test empty Comment creation"() {
        expect:
        !domain.validate()
    }

    void "Test Comment creation without profile assigned"() {
        given:
        domain.text = TEXT

        expect:
        !domain.validate()
    }

    void "Test Comment creation"() {
        given:
        domain.profile = profile

        expect:
        !domain.validate()
    }

    void "Test successful Comment creation"() {
        given:
        domain.profile = profile
        domain.text = TEXT

        expect:
        domain.validate()
    }
}
