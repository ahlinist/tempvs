package com.tempvs.services

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional

@Transactional
class UserService {
    def springSecurityService

    User getUser(String id) {
        try {
            UserProfile.findByCustomId(id)?.user ?: User.get(id as Long)
        } catch (NumberFormatException e) {}
    }

    User createUser(Map properties) {
        User user = new User(properties)

        user.with{
            password = springSecurityService.encodePassword(user.password)
            userProfile = new UserProfile(properties + [avatar: new Avatar()])
            save(flush: true)
        }
    }

    User updateEmail(String email) {
        User user = springSecurityService.currentUser
        user.email = email
        user.save()
        return user
    }

    User updatePassword(String newPassword) {
        User user = springSecurityService.currentUser
        user.password = springSecurityService.encodePassword(newPassword)
        user.save()
        return user
    }

    void updateLastActive(){
        User user = springSecurityService.currentUser

        if (user) {
            user.lastActive = new Date()
            user.save()
        }
    }
}