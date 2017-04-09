package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
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
    private static final String SHOW_PROFILE_PAGE_URI = '/userProfile'
    private static final String ID = 'id'
    private static final String EMAIL = 'email'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'

    def ajaxResponseService = Mock(AjaxResponseService)
    def userService = Mock(UserService)
    def userProfileService = Mock(UserProfileService)
    def verifyService = Mock(VerifyService)
    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def emailVerification = Mock(EmailVerification)
    def json = Mock(JSON)
    def userPasswordCommand = Mock(UserPasswordCommand)
    def registerCommand = Mock(RegisterCommand)

    def setup() {
        controller.userProfileService = userProfileService
        controller.verifyService = verifyService
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Render edit page"() {
        when:
        Map model = controller.edit()

        then:
        1 * springSecurityService.currentUser >> user
        0 * _

        and:
        model == [user: user]
    }

    void "Test index() action"() {
        when:
        controller.index()

        then:
        response.redirectedUrl == SHOW_PROFILE_PAGE_URI
    }

    void "Check updateEmail action for duplicate"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL) >> EMAIL
        0 * _
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Check updateEmail action for used email"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL)
        1 * userService.getUserByEmail(EMAIL) >> user
        0 * _
        response.json.messages == [EMAIL_USED]
    }

    void "Check updateEmail action"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(EMAIL)
        1 * userService.getUserByEmail(EMAIL)
        1 * userProfileService.getProfileByProfileEmail(EMAIL)
        1 * user.getProperty(ID) >> ID
        1 * verifyService.createEmailVerification([userId: ID, email: EMAIL, action: UPDATE_EMAIL_ACTION]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for invalid params"() {
        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(userPasswordCommand, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for valid params"() {
        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.TRUE
        1 * userPasswordCommand.newPassword >> NEW_PASSWORD
        1 * userService.updatePassword(NEW_PASSWORD) >> user
        1 * ajaxResponseService.composeJsonResponse(user, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against invalid command"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(registerCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and invalid user data"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        _ * registerCommand._
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.composeJsonResponse(user) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and valid user data"() {
        when:
        params.isAjaxRequest = Boolean.TRUE
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        _ * registerCommand._
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * springSecurityService.reauthenticate(*_)
        0 * _
        response.json.redirect == SHOW_PROFILE_PAGE_URI
    }
}
