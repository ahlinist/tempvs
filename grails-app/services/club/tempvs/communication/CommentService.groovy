package club.tempvs.communication

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@Transactional
@GrailsCompileStatic
class CommentService {

    ObjectFactory objectFactory

    Comment getComment(Long id) {
        Comment.get id
    }

    Comment loadComment(Long id) {
        Comment.load id
    }

    Comment createComment(String text, Profile profile) {
        Comment comment = objectFactory.getInstance(Comment)
        comment.text = text
        comment.profile = profile
        return comment
    }
}
