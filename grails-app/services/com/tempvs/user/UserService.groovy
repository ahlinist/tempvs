package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.item.ItemStash
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
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

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User createUser(Map properties) {
        User user = objectFactory.create(User.class)
        UserProfile userProfile = objectFactory.create(UserProfile.class)
        ItemStash itemStash = objectFactory.create(ItemStash.class)
        user.email = properties.email
        user.password = springSecurityService.encodePassword(properties.password as String)
	    userProfile.firstName = properties.firstName
	    userProfile.lastName =  properties.lastName
        user.userProfile = userProfile
        user.itemStash = itemStash
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
        User currentUser = currentUser

        User user = User.findByEmail(email)
        UserProfile userProfile = UserProfile.findByProfileEmail(email)
        ClubProfile clubProfile = ClubProfile.findByProfileEmail(email)

        (!user || currentUser == user) &&
                (!userProfile || userProfile.user == currentUser) &&
                (!clubProfile || clubProfile.user == currentUser)
    }
}
