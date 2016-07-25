package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional

@Transactional
class UserService {
    def springSecurityService

    User getUser(String id) {
        User user = UserProfile.findByCustomId(id)?.user

        if (!user) {
            user = User.get(id)
        }

        return user
    }

    User getUser(Long id) {
        User.get(id)
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
        user.lastActive = new Date()
        user.userProfile = new UserProfile(properties)
        return user.save()
    }

    User updateEmail(Long id, String email) {
        User user = getUser(id)

        if (user) {
            user.email = email
            user.save()
        }
    }

    User updateName(Long id, String firstName, String lastName) {
        User user = getUser(id)

        if (user) {
            user.firstName = firstName
            user.lastName = lastName
            user.save()
        }
    }

    User updatePassword(Long id, String password) {
        User user = getUser(id)

        if (user) {
            user.password = encodePassword(password)
            user.save()
        }
    }

    void updateLastActive(Long id){
        User user = getUser(id)

        if (user) {
            user.lastActive = new Date()
            user.save()
        }
    }
}