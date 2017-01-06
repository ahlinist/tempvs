package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import com.tempvs.services.UserService
import com.tempvs.tests.utils.TestingUtils
import com.tempvs.tests.utils.user.WithUser
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User, UserProfile, EmailVerification])
class UserControllerSpec extends Specification implements WithUser {
    private static final String EXISTENT_CUSTOM_ID = TestingUtils.CUSTOM_ID
    private static final String NON_EXISTENT_CUSTOM_ID = 'non-existentTestCustomId'
    private static final String MOCKED_RESPONSE = 'mocked_response'
    private static final String UPDATED = 'updated'
    private static final String FAKE_VER_CODE = 'verificationCode'
    private static final String NO_SUCH_USER_SHOW = 'user.show.noSuchUser.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String USER_CREATION_FAILED = 'user.register.userCreation.failed.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'
    private static final String SHOW_PAGE_URL = '/user/show'
    private static final String EDIT_USER_PAGE_URL = '/user/edit'
    private static final String EDIT_PROFILE_PAGE_URL = '/user/profile'
    private static final String LOGIN_PAGE_URL = '/auth/login'

    def setup() {
        controller.springSecurityService = [
                currentUser: user,
                reauthenticate:{arg1, arg2 -> }
        ]

        controller.userService = [
                getUser: { id ->
                    user
                },
                getUserByEmail: { email ->
                    user
                },
                updateUserProfile: { arg -> },
                createEmailVerification: { arg -> },
                createUser: { arg ->
                    user
                },
                updateEmail: { arg1 , arg2 ->
                    user
                },
                updateProfileEmail:  { arg1 , arg2 ->
                    user.userProfile
                }
        ]

        controller.ajaxResponseService = [composeJsonResponse: { obj, str = null ->
            [success: MOCKED_RESPONSE] as JSON
        }]
    }

    def cleanup() {
    }

    void "Check show page being not logged in"() {
        given:
        controller.springSecurityService = [:]

        when: 'Calling show() action'
        controller.show()

        then: 'Being redirected to login page'
        response.redirectedUrl == LOGIN_PAGE_URL
    }

    void "Check show page being logged in"() {
        when: 'Call show() action without id'
        controller.show()

        then: 'Request is redirected to login page'
        response.redirectedUrl == "${SHOW_PAGE_URL}/${EXISTENT_CUSTOM_ID}"
    }

    void "Check show page being not logged in, passing id of non-existent user"() {
        given:
        controller.userService = [getUser: { id -> }]

        when: 'Call show() action with id'
        params.id = NON_EXISTENT_CUSTOM_ID
        def model = controller.show()

        then: 'Show page is rendered with a warning'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [id: NON_EXISTENT_CUSTOM_ID, message: NO_SUCH_USER_SHOW, args: [NON_EXISTENT_CUSTOM_ID]]
    }

    void "Check show page being not logged in, passing id of existent user"() {
        given: "Mock user"
        controller.springSecurityService = [:]
        controller.userService = [getUser: { id ->
            user
        }]

        when: 'Call show() action with id'
        params.id = EXISTENT_CUSTOM_ID
        def model = controller.show()

        then: 'Show page is rendered with a warning'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [id: EXISTENT_CUSTOM_ID, user: user]
    }

    void "Check show page being logged in, passing own id"() {
        when: 'Call show() action with id'
        params.id = EXISTENT_CUSTOM_ID
        def model = controller.show()

        then: 'Request is redirected to login page'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [user:user, id: EXISTENT_CUSTOM_ID]
    }

    void "Render edit user page"() {
        when: 'Call edit()'
        def model = controller.edit()

        then: 'Edit page rendered'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [user:user]
    }

    void "Try to update user's email with duplicate"() {
        when: 'Call updateEmail() with param'
        params.email = TestingUtils.EMAIL
        controller.updateEmail()

        then: 'JSON with error message returned'
        response.text == emailUpdateDuplicateJson as String
    }

    void "Try to update user's email to non-unique one"() {
        given: 'Create additional user'
        Map paramMap = TestingUtils.DEFAULT_USER_PROPS.clone()
        paramMap.EMAIL = UPDATED + TestingUtils.EMAIL
        paramMap.PROFILE_EMAIL = UPDATED + TestingUtils.PROFILE_EMAIL
        paramMap.CUSTOM_ID = null
        TestingUtils.createUser(paramMap)

        when: 'Call updateEmail() with non-unique email'
        params.email = UPDATED + TestingUtils.EMAIL
        controller.updateEmail()

        then: 'JSON with error message returned'
        response.text == emailUsedJson as String
    }

    void "Update user's email"() {
        given: 'Mocking the userService'
        controller.userService = Mock(UserService)

        when: 'Call updateEmail() with param'
        params.email = UPDATED + TestingUtils.EMAIL
        controller.updateEmail()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Update user password incorrectly"() {
        when: 'Call updatePassword()'
        controller.updatePassword()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Render profile page"() {
        when: 'Call profile()'
        def model = controller.profile()

        then: 'Edit page rendered'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [user:user]
    }

    void "Update profile"() {
        when: 'Call updateUserProfile()'
        controller.updateUserProfile()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Try to update profileEmail with duplicate"() {
        when: 'Call updateProfileEmail() with param'
        params.profileEmail = TestingUtils.PROFILE_EMAIL
        controller.updateProfileEmail()

        then: 'JSON with error message returned'
        response.text == emailUpdateDuplicateJson as String
    }

    void "Try to update profileEmail with non-unique value"() {
        given: 'Create additional user'
        Map paramMap = TestingUtils.DEFAULT_USER_PROPS.clone()
        paramMap.EMAIL = UPDATED + TestingUtils.EMAIL
        paramMap.PROFILE_EMAIL = UPDATED + TestingUtils.PROFILE_EMAIL
        paramMap.CUSTOM_ID = null
        TestingUtils.createUser(paramMap)

        when: 'Call updateProfileEmail()'
        params.profileEmail = UPDATED + TestingUtils.PROFILE_EMAIL
        controller.updateProfileEmail()

        then: 'JSON with error message returned'
        response.text == emailUsedJson as String
    }

    void "Update profileEmail"() {
        given: 'Mocking the userService'
        controller.userService = Mock(UserService)

        when: 'Call updateProfileEmail()'
        params.profileEmail = UPDATED + TestingUtils.PROFILE_EMAIL
        controller.updateProfileEmail()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Update avatar"() {
        given: 'Mock imageService'
        controller.imageService = [updateAvatar: { arg -> }]

        when: 'Call updateAvatar()'
        controller.updateAvatar()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Get avatar"() {
        given: 'Mock imageService'
        controller.imageService = [getOwnAvatar: {new ByteArrayOutputStream().toByteArray()}]

        when: 'Call getAvatar()'
        controller.getAvatar()

        then: 'Response of image/jpg type returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        response.contentType == 'image/jpg'
    }

    void "Check verify() without id"() {
        when: 'Call verify()'
        def model = controller.verify()

        then: 'Warning returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify() with non-existent verification code"() {
        when: 'Call verify() with fake id'
        params.id = FAKE_VER_CODE
        def model = controller.verify()

        then: 'Warning returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify() with correct register user entry"() {
        when: 'Call verify() for registerUser action'
        params.id = TestingUtils.createEmailVerification().verificationCode
        controller.verify()

        then: 'Redirected to show page'
        controller.modelAndView == null
        response.redirectedUrl == SHOW_PAGE_URL
    }

    void "Check verify() with incorrect register user entry"() {
        given: 'Mock incorrect user'
        controller.userService = [
                createUser: {obj -> incorrectUser}
        ]

        when: 'Call verify() for registerUser action'
        params.id = TestingUtils.createEmailVerification().verificationCode
        def model = controller.verify()

        then: 'Warning message returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: USER_CREATION_FAILED]
    }

    void "Check verify() with correct update email entry"() {
        when: 'Call verify() for updateEmail action'
        params.id = TestingUtils.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS).verificationCode
        controller.verify()

        then: 'Redirected to edit user page'
        controller.modelAndView == null
        response.redirectedUrl == EDIT_USER_PAGE_URL
    }

    void "Check verify() with incorrect update email entry"() {
        given: 'Mock incorrect user'
        controller.userService = [
                updateEmail: { arg1, arg2 -> incorrectUser}
        ]

        when: 'Call verify() for updateEmail action'
        params.id = TestingUtils.createEmailVerification(TestingUtils.DEFAULT_EMAIL_VERIFICATION_PROPS).verificationCode
        def model = controller.verify()

        then: 'Warning message returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: EMAIL_UPDATE_FAILED]
    }

    void "Check verify() with correct update profileEmail entry"() {
        when: 'Call verify() for updateProfileEmail action'
        params.id = TestingUtils.createEmailVerification(TestingUtils.DEFAULT_PROFILE_EMAIL_VERIFICATION_PROPS).verificationCode
        controller.verify()

        then: 'Redirected to profile page'
        controller.modelAndView == null
        response.redirectedUrl == EDIT_PROFILE_PAGE_URL
    }

    void "Check verify() with incorrect update profileEmail entry"() {
        given: 'Mock incorrect user'
        controller.userService = [
                updateProfileEmail: { arg1, arg2 ->
                    UserProfile userProfile = new UserProfile()
                    userProfile.save()
                    userProfile
                }
        ]

        when: 'Call verify() for updateProfileEmail action'
        params.id = TestingUtils.createEmailVerification(TestingUtils.DEFAULT_PROFILE_EMAIL_VERIFICATION_PROPS).verificationCode
        def model = controller.verify()

        then: 'Warning message returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: PROFILE_EMAIL_UPDATE_FAILED]
    }

    private static User getIncorrectUser() {
        User incorrectUser = new User()
        incorrectUser.save()
        incorrectUser
    }

    private static JSON getEmailUpdateDuplicateJson() {
        [messages: [EMAIL_UPDATE_DUPLICATE]] as JSON
    }

    private static JSON getEmailUsedJson() {
        [messages: [EMAIL_USED]] as JSON
    }
}
