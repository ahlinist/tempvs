package club.tempvs.tests.utils

import club.tempvs.periodization.Period

/**
 * Utility class that holds test data for dummy {@link club.tempvs.user.User} and {@link club.tempvs.user.Profile} creation.
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

    public static final Map DEFAULT_CLUB_PROFILE_PROPS = [
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            nickName:       NICK_NAME,
            profileEmail:   PROFILE_EMAIL,
            location:       LOCATION,
            profileId:      PROFILE_ID,
            clubName:       CLUB_NAME,
            period:         PERIOD,
            active:         Boolean.TRUE,
    ]

    public static final Map DEFAULT_USER_PROFILE_PROPS = [
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            profileEmail:   PROFILE_EMAIL,
            location:       LOCATION,
            profileId:      PROFILE_ID,
            active:         Boolean.TRUE,
    ]
}
