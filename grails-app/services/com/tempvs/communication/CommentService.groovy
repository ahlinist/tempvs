package com.tempvs.communication

import com.tempvs.user.ClubProfile
import com.tempvs.user.Profile
import com.tempvs.user.ProfileService
import com.tempvs.user.UserProfile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@Transactional
@GrailsCompileStatic
class CommentService {

    ProfileService profileService

    Comment getComment(Long id) {
        Comment.get(id)
    }

    Comment createComment(String text) {
        Comment comment = new Comment(text: text)
        Profile profile = profileService.currentProfile

        if (profile instanceof ClubProfile) {
            comment.clubProfile = profile as ClubProfile
        } else {
            comment.userProfile = profile as UserProfile
        }

        comment
    }
}
