package com.tempvs.tests.utils

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class TestingUtils {
    public static final String EMAIL = 'defaultTest@mail.com'
    public static final String PASSWORD = 'defaultPassW0rd!'
    public static final String FIRST_NAME = 'defaultFirstName'
    public static final String LAST_NAME = 'defaultLastName'
    public static final String CUSTOM_ID = 'defaultTestCustomId'
    public static final String PROFILE_EMAIL = 'defaultProfileEmail@mail.com'
    public static final String LOCATION = 'defaultLocation'

    public static final Map DEFAULT_USER_PROPS = [
            email:          EMAIL,
            password:       PASSWORD,
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            customId:       CUSTOM_ID,
            profileEmail:   PROFILE_EMAIL,
            location:       LOCATION,
    ]

    static User createUser(Map props = DEFAULT_USER_PROPS){
        User user = new User(props)
        user.userProfile = new UserProfile(props)
        user.save(flush:true)
    }
}
