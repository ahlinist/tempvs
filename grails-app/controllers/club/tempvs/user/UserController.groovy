package club.tempvs.user

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

/**
 * Controller for managing {@link User}-related instances.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class UserController {

    private static final String PASSWORD = 'password'
    private static final String UPDATE_EMAIL_ACTION = 'email'
    private static final String PASSWORD_UPDATED_MESSAGE = 'user.edit.password.success.message'
    private static final String UPDATE_EMAIL_MESSAGE_SENT = 'user.edit.email.verification.sent.message'
    private static final String UPDATE_EMAIL_FAILED_MESSAGE = 'user.edit.email.verification.failed.message'

    static allowedMethods = [
            index: 'GET',
            edit: 'GET',
            updateEmail: 'POST',
            updatePassword: 'POST',
            register: 'POST',
    ]

    UserService userService
    VerifyService verifyService
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper
    SpringSecurityService springSecurityService

    def index() {
        redirect controller: 'profile'
    }

    def edit() {
        [user: userService.currentUser]
    }

    def updateEmail() {
        String email = params.fieldValue

        Map properties = [
                instanceId: userService.currentUserId,
                email: email,
                action: UPDATE_EMAIL_ACTION,
        ]

        EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

        if (emailVerification.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(emailVerification, UPDATE_EMAIL_FAILED_MESSAGE))
        }

        Boolean success = verifyService.sendEmailVerification(emailVerification)

        String message = success ? UPDATE_EMAIL_MESSAGE_SENT : UPDATE_EMAIL_FAILED_MESSAGE

        render ajaxResponseHelper.renderFormMessage(success, message)
    }

    def updatePassword(UserPasswordCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        User user = userService.editUserField(userService.currentUser, PASSWORD, command.newPassword)

        if (!user.hasErrors()) {
            render ajaxResponseHelper.renderFormMessage(Boolean.TRUE, PASSWORD_UPDATED_MESSAGE)
        } else {
            render ajaxResponseHelper.renderValidationResponse(user)
        }
    }

    @Secured('permitAll')
    def register(RegistrationCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        EmailVerification emailVerification = command.emailVerification
        String email = emailVerification.email
        Map properties = command.properties
        properties.email = email
        User user = userService.register(properties as User, properties as UserProfile)

        if (user.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(user))
        }

        springSecurityService.reauthenticate(email)
        emailVerification.delete(flush: true)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
