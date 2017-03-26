package com.tempvs.user

import com.tempvs.user.User
import com.tempvs.user.UserProfile
import com.tempvs.user.verification.EmailVerification
import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.UserService
import com.tempvs.user.RegisterCommand
import com.tempvs.user.UserController
import com.tempvs.user.UserPasswordCommand
import com.tempvs.user.UserProfileCommand
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
class UserControllerSpec extends Specification {
    private static final String LOGIN_PAGE_URI = '/auth/login'
    private static final String SHOW_PAGE_URI = '/user/show'
    private static final String EDIT_PAGE_URI = '/user/edit'
    private static final String PROFILE_PAGE_URI = '/user/profile'
    private static final String ID = 'id'
    private static final String USER_PROFILE = 'userProfile'
    private static final String CUSTOM_ID = 'customId'
    private static final String EMAIL = 'email'
    private static final String REGISTER_ACTION = 'register'
    private static final String USER_ID = 'userId'
    private static final String ACTION = 'action'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String UPDATE_EMAIL_ACTION = 'updateEmail'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'updateProfileEmail'
    private static final String NO_SUCH_USER = 'user.show.noSuchUser.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'user.edit.profileEmail.verification.sent.message'
    private static final String NO_VERIFICATION_CODE = 'user.register.verify.noCode.message'
    private static final String EMAIL_UPDATE_FAILED = 'user.edit.email.failed.message'
    private static final String PROFILE_EMAIL_UPDATE_FAILED = 'user.editUserProfile.failed'

    def ajaxResponseService = Mock(AjaxResponseService)
    def userService = Mock(UserService)
    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def emailVerification = Mock(EmailVerification)
    def json = Mock(JSON)
    def userPasswordCommand = Mock(UserPasswordCommand)
    def userProfileCommand = Mock(UserProfileCommand)
    def registerCommand = Mock(RegisterCommand)

    def setup() {
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Render show page being not logged in"() {
        when: 'Call show() action without id'
        controller.show()

        then: 'Request is redirected to login page'
        1 * springSecurityService.currentUser
        0 * _
        response.redirectedUrl == LOGIN_PAGE_URI
    }

    void "Render show page being logged in having customId in userProfile"() {
        when: 'Call show() action without id'
        controller.show()

        then: 'Request is redirected to show/customId page'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CUSTOM_ID) >> CUSTOM_ID
        0 * _
        response.redirectedUrl == "${SHOW_PAGE_URI}/${CUSTOM_ID}"
    }

    void "Render show page being logged in having no customId in userProfile"() {
        when: 'Call show() action without id'
        controller.show()

        then: 'Request is redirected to show/id page'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CUSTOM_ID)
        1 * user.getProperty(ID) >> ID
        0 * _
        response.redirectedUrl == "${SHOW_PAGE_URI}/${ID}"
    }

    void "Render show page for non-existent id being not logged in"() {
        when: 'Call show() action with id'
        params.id = ID
        Map model = controller.show()

        then: 'Model is passed to show page'
        1 * springSecurityService.currentUser
        1 * userService.getUser(ID)
        0 * _
        model == [id: ID, message: NO_SUCH_USER, args: [ID]]
    }

    void "Render show page for existent id being not logged in"() {
        when: 'Call show() action with id'
        params.id = ID
        Map model = controller.show()

        then: 'Model is passed to show page'
        1 * springSecurityService.currentUser
        1 * userService.getUser(ID) >> user
        1 * user.getProperty(ID) >> ID
        0 * _
        model == [user: user, id: ID]
    }

    void "Render show page for non-existent id being logged in having no customId"() {
        when: 'Call show() action with id'
        params.id = ID
        Map model = controller.show()

        then: 'Model is passed to show page'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CUSTOM_ID)
        1 * user.getProperty(ID)
        1 * user.getProperty(ID) >> ID
        1 * userService.getUser(ID) >> user
        0 * _
        model == [user: user, id: ID]
    }

    void "Render show page for non-existent id being logged in having customId"() {
        when: 'Call show() action with own customId'
        params.id = CUSTOM_ID
        Map model = controller.show()

        then: 'Model is passed to show page'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(CUSTOM_ID) >> CUSTOM_ID
        0 * _
        model == [user: user, id: CUSTOM_ID]
    }

    void "Render edit page"() {
        when: 'Call edit() action'
        Map model = controller.edit()

        then: 'Model is passed to edit page'
        1 * springSecurityService.currentUser >> user
        0 * _
        model == [user: user]
    }

    void "Check updateEmail action for duplicate"() {
        when: 'Call updateEmail() action'
        params.email = EMAIL
        controller.updateEmail()

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL) >> EMAIL
        0 * _
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Check updateEmail action for used email"() {
        when: 'Call updateEmail() action'
        params.email = EMAIL
        controller.updateEmail()

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL)
        1 * userService.getUserByEmail(EMAIL) >> user
        0 * _
        response.json.messages == [EMAIL_USED]
    }

    void "Check updateEmail action"() {
        when: 'Call updateEmail() action'
        params.email = EMAIL
        controller.updateEmail()

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL)
        1 * userService.getUserByEmail(EMAIL)
        1 * userService.getUserByProfileEmail(EMAIL)
        1 * user.getProperty(ID) >> ID
        1 * userService.createEmailVerification([userId: ID, email: EMAIL, action: UPDATE_EMAIL_ACTION]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for invalid params"() {
        when: 'Call updatePassword() action'
        controller.updatePassword(userPasswordCommand)

        then: 'JSON response received'
        1 * userPasswordCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(userPasswordCommand, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for valid params"() {
        when: 'Call updatePassword() action'
        controller.updatePassword(userPasswordCommand)

        then: 'JSON response received'
        1 * userPasswordCommand.validate() >> Boolean.TRUE
        1 * userPasswordCommand.newPassword >> NEW_PASSWORD
        1 * userService.updatePassword(NEW_PASSWORD) >> user
        1 * ajaxResponseService.composeJsonResponse(user, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateUserProfile action"() {
        when: 'Call updateUserProfile() action'
        controller.updateUserProfile(userProfileCommand)

        then: 'JSON response received'
        _ * userProfileCommand._ >> [:]
        1 * userService.updateUserProfile(_ as Map) >> userProfile
        1 * ajaxResponseService.composeJsonResponse(userProfile, USER_PROFILE_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateProfileEmail action for duplicate"() {
        when: 'Call updateProfileEmail() action'
        controller.updateProfileEmail(PROFILE_EMAIL)

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL) >> PROFILE_EMAIL
        0 * _
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Check updateProfileEmail action for used profileEmail"() {
        when: 'Call updateProfileEmail() action'
        controller.updateProfileEmail(PROFILE_EMAIL)

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL)
        1 * userService.getUserByEmail(PROFILE_EMAIL) >> user
        1 * user.getProperty(EMAIL) >> PROFILE_EMAIL
        1 * userService.getUserByProfileEmail(PROFILE_EMAIL) >> user
        0 * _
        response.json.messages == [EMAIL_USED]
    }

    void "Check updateProfileEmail action"() {
        when: 'Call updateProfileEmail() action'
        controller.updateProfileEmail(PROFILE_EMAIL)

        then: 'JSON response received'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL)
        1 * userService.getUserByEmail(PROFILE_EMAIL)
        1 * userService.getUserByProfileEmail('profileEmail')
        1 * user.getProperty(ID) >> ID
        1 * userService.createEmailVerification(
                [userId: ID, email: PROFILE_EMAIL, action: UPDATE_PROFILE_EMAIL_ACTION]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against invalid command"() {
        when: 'Call register() action'
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then: 'JSON response received'
        1 * registerCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(registerCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and invalid user data"() {
        when: 'Call register() action'
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then: 'JSON response received'
        1 * registerCommand.validate() >> Boolean.TRUE
        _ * registerCommand._
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.composeJsonResponse(user) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and valid user data"() {
        when: 'Call register() action'
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then: 'JSON response received'
        1 * registerCommand.validate() >> Boolean.TRUE
        _ * registerCommand._
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * springSecurityService.reauthenticate(*_)
        0 * _
        response.json.redirect == SHOW_PAGE_URI
    }

    void "Check verify action without id"() {
        expect: 'verify() action returned the corresponding message'
        controller.verify() == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify action with invalid id"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        Map model = controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> null
        0 * _
        model == [message: NO_VERIFICATION_CODE]
    }

    void "Check verify action for user registration"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL)
        1 * emailVerification.getProperty(USER_ID)
        1 * emailVerification.getProperty(ACTION) >> REGISTER_ACTION
        1 * emailVerification.delete(['flush':true])
        0 * _
    }

    void "Check verify action fail for email change"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        Map model = controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> 1L
        1 * emailVerification.getProperty(ACTION) >> UPDATE_EMAIL_ACTION
        1 * userService.updateEmail(1L, EMAIL) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * emailVerification.delete(['flush':true])
        0 * _
        model == [message: EMAIL_UPDATE_FAILED]
    }

    void "Check verify action success for email change"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> 1L
        1 * emailVerification.getProperty(ACTION) >> UPDATE_EMAIL_ACTION
        1 * userService.updateEmail(1L, EMAIL) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * emailVerification.delete(['flush':true])
        0 * _
        response.redirectedUrl == EDIT_PAGE_URI
    }

    void "Check verify action fail for profileEmail change"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        Map model = controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> 1L
        1 * emailVerification.getProperty(ACTION) >> UPDATE_PROFILE_EMAIL_ACTION
        1 * userService.updateProfileEmail(1L, EMAIL) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.TRUE
        1 * emailVerification.delete(['flush':true])
        0 * _
        model == [message: PROFILE_EMAIL_UPDATE_FAILED]
    }

    void "Check verify action success for profileEmail change"() {
        when: 'Call verify() acrion with id'
        params.id = ID
        controller.verify()

        then: 'If no verification found in db - the corresponding message is returned'
        1 * userService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> 1L
        1 * emailVerification.getProperty(ACTION) >> UPDATE_PROFILE_EMAIL_ACTION
        1 * userService.updateProfileEmail(1L, EMAIL) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.FALSE
        1 * emailVerification.delete(['flush':true])
        0 * _
        response.redirectedUrl == PROFILE_PAGE_URI
    }
}
