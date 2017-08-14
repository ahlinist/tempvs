package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.context.MessageSource
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
class UserControllerSpec extends Specification {
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String PROPERTIES = 'properties'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String DIFFERENT_EMAIL = 'differentEmail'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'

    def ajaxResponseService = Mock(AjaxResponseService)
    def userService = Mock(UserService)
    def verifyService = Mock(VerifyService)
    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def emailVerification = Mock(EmailVerification)
    def json = Mock(JSON)
    def userPasswordCommand = Mock(UserPasswordCommand)
    def registerCommand = Mock(RegisterCommand)
    def messageSource = Mock(MessageSource)

    def setup() {
        controller.verifyService = verifyService
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.springSecurityService = springSecurityService
        controller.messageSource = messageSource
    }

    def cleanup() {
    }

    void "Render edit page"() {
        when:
        Map model = controller.edit()

        then:
        1 * userService.currentUser >> user
        0 * _

        and:
        model == [user: user]
    }

    void "Test index() action"() {
        when:
        controller.index()

        then:
        response.redirectedUrl == PROFILE_PAGE_URI
    }

    void "Check updateEmail() for duplicate"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> EMAIL
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_UPDATE_DUPLICATE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateEmail action for used email"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.FALSE
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_USED) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateEmail action"() {
        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.TRUE
        1 * userService.currentUserId >> LONG_ID
        1 * verifyService.createEmailVerification([instanceId: LONG_ID, email: EMAIL, action: UPDATE_EMAIL_ACTION]) >> emailVerification
        1 * ajaxResponseService.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for invalid params"() {
        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(userPasswordCommand, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updatePassword action for valid params"() {
        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.TRUE
        1 * userPasswordCommand.getNewPassword() >> NEW_PASSWORD
        1 * userService.updatePassword(NEW_PASSWORD) >> user
        1 * ajaxResponseService.renderValidationResponse(user, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against invalid command"() {
        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(registerCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and invalid user data"() {
        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        1 * registerCommand.getProperty(PROPERTIES) >> [:]
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.renderValidationResponse(user) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check register action against valid command and valid user data"() {
        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        1 * registerCommand.getProperty(PROPERTIES) >> [:]
        1 * registerCommand.getPassword()
        1 * userService.createUser(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * springSecurityService.reauthenticate(*_)
        1 * ajaxResponseService.renderRedirect(PROFILE_PAGE_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
