package com.tempvs.user

import grails.transaction.Transactional

@Transactional
class UserService {
    def springSecurityService

    User getUser(String id) {
        try {
            UserProfile.findByProfileId(id)?.user ?: User.get(id as Long)
        } catch (NumberFormatException e) {}
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User createUser(Map properties) {
        User user = new User(properties)

        user.with{
            password = springSecurityService.encodePassword(user.password)
            userProfile = new UserProfile(properties)
            save(flush: true)
        }

        user
    }

    User updateEmail(Long userId, String email) {
        User user = User.get(userId)

        if (user) {
            user.email = email
            user.save(flush: true)
        }

        user
    }

    User updatePassword(String newPassword) {
        User user = springSecurityService.currentUser
        user.password = springSecurityService.encodePassword(newPassword)
        user.save(flush: true)
        user
    }

    void updateLastActive(){
        User user = springSecurityService.currentUser
        user.lastActive = new Date()
        user.save(flush: true)
    }
}
