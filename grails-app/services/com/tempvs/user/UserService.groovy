package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

@Transactional
@GrailsCompileStatic
class UserService {

    SpringSecurityService springSecurityService
    ObjectDAO objectDAO

    Object getUser(String id) {
        objectDAO.find(UserProfile, [profileId: id]) ?: objectDAO.get(User, id)
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User createUser(Map properties) {
        properties.password = springSecurityService.encodePassword(properties.password as String)
        properties.userProfile = new UserProfile(properties)
        User user = new User(properties)
        user.save()

        user
    }

    User updateEmail(User user, String email) {
        user.email = email
        user.save()
        user
    }

    User updatePassword(String newPassword) {
        User user = springSecurityService.currentUser as User
        user.password = springSecurityService.encodePassword(newPassword)
        user.save()
        user
    }

    void updateLastActive(){
        User user = springSecurityService.currentUser as User

        if (user) {
            user.lastActive = new Date()
            user.save()
        }
    }

    Boolean isEmailUnique(String email) {
        User currentUser = springSecurityService.currentUser as User

        User user = User.findByEmail(email)
        UserProfile userProfile = UserProfile.findByProfileEmail(email)
        ClubProfile clubProfile = ClubProfile.findByProfileEmail(email)

        (!user || currentUser == user) &&
                (!userProfile || userProfile.user == currentUser) &&
                (!clubProfile || clubProfile.user == currentUser)
    }
}
