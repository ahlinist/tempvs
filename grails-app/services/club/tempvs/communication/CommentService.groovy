package club.tempvs.communication

import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@Transactional
@GrailsCompileStatic
class CommentService {

    Comment getComment(Long id) {
        Comment.get id
    }

    Comment loadComment(Long id) {
        Comment.load id
    }

    Comment createComment(String text, Profile profile) {
        Comment comment = new Comment(text: text, profile: profile)
        comment
    }
}
