package club.tempvs.auth

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.rest.RestResponse
import club.tempvs.user.EmailVerification
import club.tempvs.user.User
import club.tempvs.user.UserService
import club.tempvs.user.VerifyService
import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices

/**
 * Controller that manages {@link User} registration and authorization.
 */
@Secured('permitAll')
@GrailsCompileStatic
class AuthController {

    private static final String REFERER = 'referer'
    private static final String AUTH_PATTERN = '/auth'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String NO_SUCH_USER = 'auth.login.noSuchUser.message'
    private static final String REGISTER_MESSAGE_SENT = 'auth.register.sent.message'
    private static final String REGISTER_MESSAGE_NOT_SENT = 'auth.register.notsent.message'

    static allowedMethods = [index: 'GET', login: 'POST', register: 'POST']

    UserService userService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AjaxResponseHelper ajaxResponseHelper
    PasswordEncoder passwordEncoder
    LinkGenerator grailsLinkGenerator
    TokenBasedRememberMeServices rememberMeServices

    def index() {
    }

    def login(LoginCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        String email = command.email
        String password = command.password
        User user = userService.getUserByEmail(email)

        if (!user || !passwordEncoder.matches(password, user.password)) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, NO_SUCH_USER))
        }

        springSecurityService.reauthenticate(email, password)

        if (command.remember) {
            rememberMeServices.onLoginSuccess(request, response, springSecurityService.authentication)
        }

        String refererLink = request.getHeader(REFERER)

        if (refererLink.contains(AUTH_PATTERN)) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile'))
        } else {
            render ajaxResponseHelper.renderRedirect(refererLink)
        }
    }

    def register(RequestRegistrationCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        Map properties = [action: REGISTRATION_ACTION, email: command.email]
        EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

        if (emailVerification.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(emailVerification, REGISTER_MESSAGE_NOT_SENT))
        }

        RestResponse restResponse = verifyService.sendEmailVerification(emailVerification)

        if (restResponse.statusCode == 200) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.TRUE, REGISTER_MESSAGE_SENT))
        } else {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, REGISTER_MESSAGE_NOT_SENT))
        }
    }

    def logout() {
        rememberMeServices.logout(request, response, null)
        session.invalidate()
        redirect uri: request.getHeader(REFERER)
    }
}
