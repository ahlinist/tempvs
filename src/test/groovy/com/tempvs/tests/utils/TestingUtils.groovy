package com.tempvs.tests.utils

import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserProfile

/**
 * Utility class that encapsulates user-related operations to simplify
 * application unit-testing.
 */
class TestingUtils {
    public static final String EMAIL = 'defaultTest@mail.com'
    public static final String PASSWORD = 'defaultPassW0rd!'
    public static final String FIRST_NAME = 'defaultFirstName'
    public static final String LAST_NAME = 'defaultLastName'
    public static final String NICK_NAME = 'defaultNickName'
    public static final String PROFILE_ID = 'defaultTestProfileId'
    public static final String PROFILE_EMAIL = 'defaultProfileEmail@mail.com'
    public static final String LOCATION = 'defaultLocation'
    public static final String CLUB_NAME = 'defaultClubName'
    public static final Period PERIOD = Period.ANCIENT

    public static final Map DEFAULT_USER_PROPS = [
            email:          EMAIL,
            password:       PASSWORD,
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            profileId:       PROFILE_ID,
            profileEmail:   PROFILE_EMAIL,
            location:       LOCATION,
    ]

    public static final Map DEFAULT_CLUB_PROFILE_PROPS = [
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            nickName:       NICK_NAME,
            profileEmail:   EMAIL,
            location:       LOCATION,
            profileId:      PROFILE_ID,
            clubName:       CLUB_NAME,
            period: PERIOD,
    ]

    static User createUser(Map props = DEFAULT_USER_PROPS){
        User user = new User(props)
        UserProfile userProfile = new UserProfile(props)
        user.userProfile = userProfile
        userProfile.user = user
        user.save(flush:true)
    }
}
