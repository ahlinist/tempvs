package com.tempvs.user

import com.tempvs.ajax.AjaxResponseHelper
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class UserControllerSpec extends Specification implements ControllerUnitTest<UserController> {

    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String POST_METHOD = 'POST'
    private static final String PASSWORD = 'password'
    private static final String NEW_PASSWORD = 'newPassword'
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'

    def user = Mock User
    def json = Mock JSON
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def userService = Mock UserService
    def verifyService = Mock VerifyService
    def emailVerification = Mock EmailVerification
    def ajaxResponseHelper = Mock AjaxResponseHelper
    def userPasswordCommand = Mock UserPasswordCommand
    def registrationCommand = Mock RegistrationCommand
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        controller.userService = userService
        controller.verifyService = verifyService
        controller.ajaxResponseHelper = ajaxResponseHelper
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

    void "Test updateEmail()"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        params.email = EMAIL
        controller.updateEmail()

        then:
        1 * userService.currentUserId >> LONG_ID
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification)
        1 * ajaxResponseHelper.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT) >> json
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
        1 * ajaxResponseHelper.renderValidationResponse(userPasswordCommand) >> json
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
        1 * ajaxResponseHelper.renderFormMessage(Boolean.TRUE, PASSWORD_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test register() against invalid user"() {
        given:
        request.method = POST_METHOD
        params.password = PASSWORD
        params.repeatedPassword = PASSWORD
        controller.session.email = EMAIL

        when:
        controller.register(registrationCommand)

        then:
        1 * registrationCommand.validate() >> Boolean.TRUE
        2 * registrationCommand.emailVerification >> emailVerification
        1 * registrationCommand.errors
        1 * registrationCommand.lastName
        2 * registrationCommand.password >> PASSWORD
        1 * registrationCommand.confirmPassword
        1 * registrationCommand.firstName
        1 * emailVerification.email >> EMAIL
        1 * springSecurityService.encodePassword(PASSWORD) >> PASSWORD
        1 * userService.register(_ as User, _ as UserProfile) >> user
        1 * user.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(user) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test register()"() {
        given:
        request.method = POST_METHOD
        params.password = PASSWORD
        params.repeatedPassword = PASSWORD
        controller.session.email = EMAIL

        when:
        controller.register(registrationCommand)

        then:
        1 * registrationCommand.validate() >> Boolean.TRUE
        2 * registrationCommand.emailVerification >> emailVerification
        1 * registrationCommand.errors
        1 * registrationCommand.lastName
        2 * registrationCommand.password >> PASSWORD
        1 * registrationCommand.confirmPassword
        1 * registrationCommand.firstName
        1 * emailVerification.email >> EMAIL
        1 * springSecurityService.encodePassword(PASSWORD) >> PASSWORD
        1 * userService.register(_ as User, _ as UserProfile) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * springSecurityService.reauthenticate(EMAIL)
        1 * emailVerification.delete([flush: Boolean.TRUE])
        1 * ajaxResponseHelper.renderRedirect(PROFILE_PAGE_URI) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
