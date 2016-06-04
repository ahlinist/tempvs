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
        User user = UserProfile.findByCustomId(id)?.user

        if (!user) {
            user = User.get(id)
        }

        return user
    }

    Boolean checkIfUserExists(email) {
        User.findByEmail(email) || UserProfile.findByProfileEmail(email)
    }

    User createUser(props) {
        User user = new User(props)
        user.userProfile = new UserProfile(props)
        return user
    }

    def saveUserProfile(Long userProfileId, Map params) {
        List profileProps = ['firstName', 'lastName', 'profileEmail', 'location', 'customId']
        UserProfile userProfile = UserProfile.get(userProfileId)

        profileProps.each {
            userProfile."${it}" = params."${it}"
        }

        userProfile.save()
    }
}