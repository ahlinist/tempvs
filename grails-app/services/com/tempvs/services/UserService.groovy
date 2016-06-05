package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class UserService {
    User getUser(String email, String password) {
        User.findByEmailAndPassword(email, password.encodeAsMD5())
    }

    User getUser(String id) {
        User user = UserProfile.findByCustomId(id)?.user

        if (!user) {
            user = User.get(id)
        }

        return user
    }

    Boolean checkIfUserExists(String email) {
        User.findByEmail(email) || UserProfile.findByProfileEmail(email)
    }

    User createUser(command) {
        User user = new User(command)
        user.password = user.password.encodeAsMD5()
        user.userProfile = new UserProfile()
        return user
    }

    UserProfile saveUserProfile(Long id, Map params) {
        UserProfile userProfile = UserProfile.get(id)

        if (userProfile) {
            params = params.each{
                if (!it.value) {
                    it.value = null
                }
            }

            InvokerHelper.setProperties(userProfile, params)
            return userProfile.save()
        } else {
            return null
        }
    }
}