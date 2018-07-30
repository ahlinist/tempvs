package club.tempvs.user

import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.gorm.transactions.Transactional
import club.tempvs.rest.RestCaller
import grails.web.mapping.LinkGenerator
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus

/**
 * A service that handles operations related to {@link EmailVerification}.
 */
@Slf4j
@Transactional
@GrailsCompileStatic
class VerifyService {

    private static final String EMAIL_FIELD = 'email'
    private static final String EMAIL_ACTION = 'email'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String PROFILE_EMAIL_ACTION = 'profileEmail'
    private static final String EMAIL_USED_CODE = 'emailVerification.email.used.error'
    private static final String EMAIL_SERVICE_URL = System.getenv('EMAIL_SERVICE_URL')
    private static final String SEND_EMAIL_API_URI = '/api/send'
    private static final String EMAIL_SECURITY_TOKEN = System.getenv('EMAIL_SECURITY_TOKEN')

    UserService userService
    ProfileService profileService
    RestCaller restCaller
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator

    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
    }

    EmailVerification getRegistrationVerificationByUser(User user) {
        EmailVerification.findByActionAndEmail(REGISTRATION_ACTION, user.email)
    }

    EmailVerification createEmailVerification(EmailVerification emailVerification) {
        String email = emailVerification.email

        if (isEmailUnique(email, emailVerification.action, emailVerification.instanceId)) {
            emailVerification.verificationCode = (email + new Date().time).encodeAsMD5()
            emailVerification.save()
        } else {
            emailVerification.errors.rejectValue(EMAIL_FIELD, EMAIL_USED_CODE, [email] as Object[], EMAIL_USED_CODE)
        }

        emailVerification
    }

    Boolean sendEmailVerification(EmailVerification emailVerification) {
        String serverUrl = grailsLinkGenerator.serverBaseURL
        String verificationCode = emailVerification.verificationCode
        String body = groovyPageRenderer.render(view: "/verify/emailTemplates/${emailVerification.action}",
                model: [serverUrl: serverUrl, verificationCode: verificationCode])
        JSON payload = [email: emailVerification.email, subject: 'Tempvs', body: body] as JSON
        String emailServiceUrl = EMAIL_SERVICE_URL + SEND_EMAIL_API_URI
        String token = EMAIL_SECURITY_TOKEN?.encodeAsMD5() as String
        RestResponse response = restCaller.doPost(emailServiceUrl, token, payload)
        HttpStatus statusCode = response?.statusCode
        Boolean success = Boolean.TRUE

        if (statusCode != HttpStatus.OK) {
            success = Boolean.FALSE
            log.error "Email delivery failed. Email service returned status code: '${statusCode.value()}'.\n" +
                    " Response body: ${response.responseBody}"
        }

        return success
    }

    private boolean isEmailUnique(String email, String action, Long instanceId) {
        switch (action) {
            case REGISTRATION_ACTION:
                return userService.isEmailUnique(email, instanceId)

                break
            case EMAIL_ACTION:
                return userService.isEmailUnique(email, instanceId)

                break
            case PROFILE_EMAIL_ACTION:
                Profile currentProfile = profileService.getProfileById(instanceId)
                return profileService.isProfileEmailUnique(currentProfile, email)

                break
            default:
                throw new IllegalArgumentException("Unsupported action!")
                break
        }

        return true
    }
}
