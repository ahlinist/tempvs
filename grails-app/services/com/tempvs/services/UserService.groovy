package com.tempvs.services

import com.tempvs.controllers.UserRegisterCommand
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class UserService {
    User getUser(String email, String password) {
        User.findByEmailAndPassword(email, encrypt(password))
    }

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

    String encrypt(String password) {
        password.encodeAsMD5()
    }

    Boolean checkIfUserExists(String email) {
        User.findByEmail(email) || UserProfile.findByProfileEmail(email)
    }

    User createUser(UserRegisterCommand command) {
        User user = new User(command)
        user.password = encrypt(user.password)
        user.lastActive = new Date()
        user.userProfile = new UserProfile()
        return user
    }

    User updateUserProfile(Long id, Map params) {
        User user = getUser(id)

        if (user?.userProfile) {
            InvokerHelper.setProperties(user?.userProfile, params.findAll{ it.value })
            user.save()
        } else {
            return null
        }
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
            user.password = encrypt(password)
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