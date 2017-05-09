package com.tempvs.tests.utils

import com.tempvs.item.ItemStash
import com.tempvs.user.ClubProfile
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
    ]

    static User createUser(Map props = DEFAULT_USER_PROPS){
        User user = new User(props)
        user.userProfile = new UserProfile(props)
        user.itemStash = new ItemStash()
        user.save(flush:true)
    }

    static User addClubProfile(User user, Map props = DEFAULT_CLUB_PROFILE_PROPS) {
        ClubProfile clubProfile = new ClubProfile(props)
        user.addToClubProfiles clubProfile
        user.save(flush: true)
    }
}
