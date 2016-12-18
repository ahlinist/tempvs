package com.tempvs.tests.unit

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class UnitTestUtils {
    public static final String CUSTOM_ID = 'defaultTestCustomId'
    public static final String EMAIL = 'defaultTest@mail.com'
    public static final String PASSWORD = 'defaultPassW0rd!'
    public static final String FIRST_NAME = 'defaultFirstName'
    public static final String LAST_NAME = 'defaultLastName'
    public static final String LOCATION = 'defaultLocation'

    static User createUser(String email = EMAIL,String password = PASSWORD, String firstName = FIRST_NAME,
                           String lastName = LAST_NAME, String customId = CUSTOM_ID, String profileEmail = null,
                           String location = LOCATION){
        User user = new User(email: email, password: password, lastActive: new Date())
        user.userProfile = new UserProfile(firstName:firstName, lastName: lastName, customId:customId,
                profileEmail: profileEmail, location:location, avatar: new Avatar())
        user.save(flush:true)
    }
}
