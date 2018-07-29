package club.tempvs.communication

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class CommentServiceSpec extends Specification implements ServiceUnitTest<CommentService>, DomainUnitTest<Comment> {

    private static final String TEXT = 'text'

    def profile = Mock Profile
    def comment = Mock Comment
    def objectFactory = Mock ObjectFactory

    def setup() {
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test createComment()"() {
        when:
        def result = service.createComment(TEXT, profile)

        then:
        1 * objectFactory.getInstance(Comment) >> comment
        1 * comment.setProfile(profile)
        1 * comment.setText(TEXT)
        0 * _

        and:
        result == comment
    }
}
