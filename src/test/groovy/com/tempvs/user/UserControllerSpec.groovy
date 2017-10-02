package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([User, UserService])
@TestFor(UserController)
class UserControllerSpec extends Specification {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String POST_METHOD = 'POST'
    private static final String PASSWORD = 'password'
    private static final String PROPERTIES = 'properties'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String DIFFERENT_EMAIL = 'differentEmail'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'

    def user = Mock User
    def json = Mock JSON
    def userProfile = Mock UserProfile
    def userService = Mock UserService
    def verifyService = Mock VerifyService
    def registerCommand = Mock RegisterCommand
    def emailVerification = Mock EmailVerification
    def ajaxResponseService = Mock AjaxResponseService
    def userPasswordCommand = Mock UserPasswordCommand
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        controller.userService = userService
        controller.verifyService = verifyService
        controller.ajaxResponseService = ajaxResponseService
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Test edit()"() {
        when:
        Map model = controller.edit()

        then:
        1 * userService.currentUser >> user
        0 * _

        and:
        model == [user: user]
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        response.redirectedUrl == PROFILE_PAGE_URI
    }

    void "Test updateEmail() for current email"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> EMAIL
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_UPDATE_DUPLICATE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateEmail() against non-unique email"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.FALSE
        1 * ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_USED) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateEmail()"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * userService.currentUserEmail >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.TRUE
        1 * userService.currentUserId >> LONG_ID
        1 * verifyService.createEmailVerification([instanceId: LONG_ID, email: EMAIL, action: UPDATE_EMAIL_ACTION]) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification)
        1 * ajaxResponseService.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updatePassword() against invalid params"() {
        given:
        request.method = POST_METHOD

        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(userPasswordCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updatePassword()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.updatePassword(userPasswordCommand)

        then:
        1 * userPasswordCommand.validate() >> Boolean.TRUE
        1 * userPasswordCommand.newPassword >> NEW_PASSWORD
        1 * springSecurityService.encodePassword(NEW_PASSWORD) >> NEW_PASSWORD
        1 * userService.currentUser >> user
        1 * userService.editUserField(user, PASSWORD, NEW_PASSWORD) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseService.renderFormMessage(Boolean.TRUE, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test register() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.renderValidationResponse(registerCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test register() against valid command but invalid user"() {
        given:
        request.method = POST_METHOD

        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        1 * registerCommand.getProperty(PROPERTIES) >> [:]
        1 * userService.register(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseService.renderValidationResponse(_ as User) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test register()"() {
        given:
        request.method = POST_METHOD
        controller.session.email = EMAIL

        when:
        controller.register(registerCommand)

        then:
        1 * registerCommand.validate() >> Boolean.TRUE
        1 * registerCommand.getProperty(PROPERTIES) >> [:]
        1 * userService.register(_ as Map) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * springSecurityService.reauthenticate(EMAIL)
        1 * ajaxResponseService.renderRedirect(PROFILE_PAGE_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
