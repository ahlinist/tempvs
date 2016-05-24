package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import grails.transaction.Transactional

@Transactional
class UserService {
    User getUser(String email, String password) {
        User.findByEmailAndPassword(email, password)
    }

    User getUser(String id) {
        User.get(id)
    }

    Boolean checkIfUserExists(email) {
        User.findByEmail(email) || UserProfile.findByProfileEmail(email)
    }

    User createUser(String email, String password) {
        return new User(email: email, password: password, userProfile: new UserProfile()).save()
    }

    def saveUserProfile(Long userProfileId, Map params) {
        List profileProps = ['firstName', 'lastName', 'profileEmail', 'location']
        UserProfile userProfile = UserProfile.get(userProfileId)

        profileProps.each {
            userProfile."${it}" = params."${it}"
        }

        userProfile.save()
    }
}