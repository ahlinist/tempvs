package club.tempvs.communication

import club.tempvs.user.ClubProfile
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class CommentServiceSpec extends Specification implements ServiceUnitTest<CommentService>, DomainUnitTest<Comment> {

    private static final String TEXT = 'text'

    def clubProfile = Mock ClubProfile

    def setup() {
    }

    def cleanup() {
    }

    void "Test createComment()"() {
        when:
        def result = service.createComment(TEXT, clubProfile)

        then:
        1 * clubProfile.asType(ClubProfile) >> clubProfile
        0 * _

        and:
        result instanceof Comment
    }
}
