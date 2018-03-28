package club.tempvs.user

import club.tempvs.ajax.AjaxResponseHelper
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
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

    static allowedMethods = [
            index: 'GET',
            edit: 'GET',
            updateEmail: 'POST',
            updatePassword: 'POST',
            register: 'POST',
    ]

    UserService userService
    VerifyService verifyService
    UserInfoHelper userInfoHelper
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper
    SpringSecurityService springSecurityService

    def index() {
        redirect controller: 'profile'
    }

    def edit() {
        [user: userInfoHelper.getCurrentUser(request)]
    }

    def updateEmail() {
        String email = params.fieldValue

        Map properties = [
                instanceId: userService.currentUserId,
                email: email,
                action: UPDATE_EMAIL_ACTION,
        ]

        EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

        if (!emailVerification.hasErrors()) {
            verifyService.sendEmailVerification(emailVerification)
        }

        render ajaxResponseHelper.renderValidationResponse(emailVerification, UPDATE_EMAIL_MESSAGE_SENT)
    }

    def updatePassword(UserPasswordCommand command) {
        if (command.validate()) {
            String password = springSecurityService.encodePassword(command.newPassword)
            User user = userService.editUserField(userInfoHelper.getCurrentUser(request), PASSWORD, password)

            if (!user.hasErrors()) {
                render ajaxResponseHelper.renderFormMessage(Boolean.TRUE, PASSWORD_UPDATED_MESSAGE)
            } else {
                render ajaxResponseHelper.renderValidationResponse(user)
            }
        } else {
            render ajaxResponseHelper.renderValidationResponse(command)
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
