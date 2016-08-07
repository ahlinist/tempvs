package com.tempvs.services

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

    String encodePassword(String password) {
        springSecurityService.encodePassword(password)
    }

    Boolean checkIfUserExists(String email) {
        User.findByEmail(email) || UserProfile.findByProfileEmail(email)
    }

    User createUser(Map properties) {
        User user = new User(properties)
        user.password = encodePassword(user.password)
        user.userProfile = new UserProfile(properties)
        return user.save()
    }

    User updateEmail(String email) {
        User user = springSecurityService.currentUser

        if (user) {
            user.email = email
            user.save()
        }
    }

    User updatePassword(String newPassword) {
        User user = springSecurityService.currentUser

        if (user) {
            user.password = encodePassword(newPassword)
            user.save()
        }
    }

    void updateLastActive(){
        User user = springSecurityService.currentUser

        if (user) {
            user.lastActive = new Date()
            user.save()
        }
    }
}