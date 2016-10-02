package com.tempvs.tests.unit

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class UnitTestUtils {
    public static void createUser(String email, String password, String firstName = 'firstName', String lastName = 'lastName',
                                  String customId = null, String profileEmail = null, String location = null){
        User user = new User(email: email, password: password, lastActive: new Date())
        user.userProfile = new UserProfile(firstName:firstName, lastName: lastName, customId:customId,
                profileEmail: profileEmail, location:location, avatar: new Avatar())
        user.save(flush:true)
    }
}
