package club.tempvs.communication

import club.tempvs.user.ClubProfile
import club.tempvs.user.Profile
import club.tempvs.user.UserProfile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@Transactional
@GrailsCompileStatic
class CommentService {

    Comment getComment(Long id) {
        Comment.get(id)
    }

    Comment createComment(String text, Profile profile) {
        Comment comment = new Comment(text: text)

        if (profile instanceof ClubProfile) {
            comment.clubProfile = profile as ClubProfile
        } else {
            comment.userProfile = profile as UserProfile
        }

        comment
    }
}
