package com.tempvs.tests.utils

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification

class TestingUtils {
    public static final String EMAIL = 'defaultTest@mail.com'
    public static final String PASSWORD = 'defaultPassW0rd!'
    public static final String FIRST_NAME = 'defaultFirstName'
    public static final String LAST_NAME = 'defaultLastName'
    public static final String CUSTOM_ID = 'defaultTestCustomId'
    public static final String PROFILE_EMAIL = 'defaultProfileEmail@mail.com'
    public static final String LOCATION = 'defaultLocation'
    public static final String DESTINATION = 'defaultDestination@email.com'
    public static final String UPDATE_EMAIL_DESTINATION = 'defaultUpdateEmailDestination@email.com'
    public static final String UPDATE_PROFILE_EMAIL_DESTINATION = 'defaultUpdateProfileEmailDestination@email.com'
    public static final String REGISTER_USER_ACTION = 'registerUser'
    public static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    public static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    public static final Long USER_ID = 1L

    public static final Map DEFAULT_USER_PROPS = [
            email:          EMAIL,
            password:       PASSWORD,
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
            customId:       CUSTOM_ID,
            profileEmail:   PROFILE_EMAIL,
            location:       LOCATION,
    ]
    public static final Map DEFAULT_REGISTER_VERIFICATION_PROPS = [
            destination:    DESTINATION,
            action:         REGISTER_USER_ACTION,
            email:          DESTINATION,
            password:       PASSWORD,
            firstName:      FIRST_NAME,
            lastName:       LAST_NAME,
    ]
    public static final Map DEFAULT_EMAIL_VERIFICATION_PROPS = [
            userId:         USER_ID,
            destination:    UPDATE_EMAIL_DESTINATION,
            action:         UPDATE_EMAIL_ACTION,
    ]

    public static final Map DEFAULT_PROFILE_EMAIL_VERIFICATION_PROPS = [
            userId:         USER_ID,
            destination:    UPDATE_PROFILE_EMAIL_DESTINATION,
            action:         UPDATE_PROFILE_EMAIL_ACTION,
    ]

    static User createUser(Map props = DEFAULT_USER_PROPS){
        User user = new User(props)
        user.userProfile = new UserProfile(props + [avatar: new Avatar()])
        user.save(flush:true)
    }

    static EmailVerification createEmailVerification(Map props = DEFAULT_REGISTER_VERIFICATION_PROPS){
        new EmailVerification(props + [verificationCode: composeVerificationCode(props)]).save(flush: true)
    }

    private static String composeVerificationCode(Map props) {
        props.verificationCode ?: props.destination.encodeAsMD5() + new Date().time
    }
}
