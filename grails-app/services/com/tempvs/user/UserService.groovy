package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional

/**
 * Service that manages operations with {@link com.tempvs.user.User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    SpringSecurityService springSecurityService
    ObjectDAO objectDAO
    ObjectFactory objectFactory

    User getUser(String id) {
        objectDAO.get(User, id)
    }

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    String getCurrentUserEmail() {
        ((GrailsUser) springSecurityService.principal).username
    }

    String getCurrentUserPassword() {
        ((GrailsUser) springSecurityService.principal).password
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User createUser(Map properties) {
        User user = objectFactory.create(User.class)
        UserProfile userProfile = objectFactory.create(UserProfile.class)
        user.email = properties.email
        user.password = springSecurityService.encodePassword(properties.password as String)
	    userProfile.firstName = properties.firstName
	    userProfile.lastName =  properties.lastName
        user.userProfile = userProfile
        user.save()
        user
    }

    User updateEmail(Long id, String email) {
        User user = objectDAO.get(User, id)
        user.email = email
        user.save()
        user
    }

    User updatePassword(String newPassword) {
        User user = currentUser
        user.password = springSecurityService.encodePassword(newPassword)
        user.save()
        user
    }

    void updateLastActive(){
        User user = currentUser

        if (user) {
            user.lastActive = new Date()
            user.save()
        }
    }

    Boolean isEmailUnique(String email) {
        if (currentUserEmail == email) {
            Boolean.TRUE
        } else {
            UserProfile userProfile = UserProfile.findByProfileEmail(email)
            ClubProfile clubProfile = ClubProfile.findByProfileEmail(email)

            (!userProfile || userProfile.user.email == currentUserEmail) &&
                    (!clubProfile || clubProfile.user.email == currentUserEmail)
        }
    }
}
