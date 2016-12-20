package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import com.tempvs.tests.unit.UnitTestUtils
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User, UserProfile, EmailVerification])
class UserControllerSpec extends Specification {
    private static final String EXISTENT_CUSTOM_ID = UnitTestUtils.CUSTOM_ID
    private static final String NON_EXISTENT_CUSTOM_ID = 'non-existentTestCustomId'
    private static final String MOCKED_RESPONSE = 'mocked_response'
    private static final String UPDATED = 'updated'
    private static final String FAKE_VER_CODE = 'verificationCode'
    private static final String NO_SUCH_USER_SHOW = 'user.show.noSuchUser.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String SHOW_PAGE_URL = '/user/show'
    private static final String LOGIN_PAGE_URL = '/auth/login'

    private static User mockedUser

    def setup() {
        controller.springSecurityService = [
                currentUser: user,
                reauthenticate:{arg1, arg2 -> }
        ]

        controller.userService = [
                getUser: { id ->
                    user
                },
                updateUserProfile: { obj -> },
                createEmailVerification: {obj -> },
                createUser: {obj -> user}
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

    void "Update user email with duplicate"() {
        when: 'Call updateEmail() with param'
        params.email = UnitTestUtils.EMAIL
        controller.updateEmail()

        then: 'Warning message returned'
        response.text == emailUpdateDuplicateJson as String
    }

    void "Update user email"() {
        when: 'Call updateEmail() with param'
        params.email = UPDATED + UnitTestUtils.EMAIL
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

    void "Update profileEmail"() {
        when: 'Call updateProfileEmail()'
        controller.updateProfileEmail()

        then: 'Mocked JSON response returned'
        response.json.success == MOCKED_RESPONSE
    }

    void "Update avatar"() {
        given: 'Mock imageService'
        controller.imageService = [updateAvatar: { obj -> }]

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

        then: 'Mocked JSON response returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        response.contentType == 'image/jpg'
    }

    void "Check verify() without id"() {
        when: 'Call verify()'
        def model = controller.verify()

        then: 'Mocked JSON response returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify() with non-existent verification code"() {
        when: 'Call verify() with fake id'
        params.id = FAKE_VER_CODE
        def model = controller.verify()

        then: 'Mocked JSON response returned'
        controller.modelAndView == null
        response.redirectedUrl == null
        model == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify() with register user entry"() {
        when: 'Call verify() with registerUser verification code'
        params.id = UnitTestUtils.createEmailVerification().verificationCode
        controller.verify()

        then: 'Mocked JSON response returned'
        controller.modelAndView == null
        response.redirectedUrl == SHOW_PAGE_URL
    }

    private static JSON getEmailUpdateDuplicateJson() {
        [messages: [EMAIL_UPDATE_DUPLICATE]] as JSON
    }

    private static User getUser() {
        if (!this.mockedUser) {
            this.mockedUser = UnitTestUtils.createUser()
        }

        return this.mockedUser
    }
}
