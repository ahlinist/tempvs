package club.tempvs.user

import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.gorm.transactions.Transactional
import club.tempvs.rest.RestCallService
import grails.web.mapping.LinkGenerator

/**
 * A service that handles operations related to {@link EmailVerification}.
 */
@Transactional
@GrailsCompileStatic
class VerifyService {

    private static final String EMAIL_FIELD = 'email'
    private static final String EMAIL_ACTION = 'email'
    private static final String USER_PROFILE_ACTION = 'userProfile'
    private static final String CLUB_PROFILE_ACTION = 'clubProfile'
    private static final String REGISTRATION_ACTION = 'registration'
    private static final String EMAIL_USED_CODE = 'emailVerification.email.used.error'
    private static final String EMAIL_SERVICE_URL = System.getenv('EMAIL_SERVICE_URL')
    private static final String SEND_EMAIL_API_URI = '/api/send'
    private static final String EMAIL_SECURITY_TOKEN = System.getenv('EMAIL_SECURITY_TOKEN')

    UserService userService
    ProfileService profileService
    RestCallService restCallService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator


    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
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

    RestResponse sendEmailVerification(EmailVerification emailVerification) {
        String serverUrl = grailsLinkGenerator.serverBaseURL
        String verificationCode = emailVerification.verificationCode
        String body = groovyPageRenderer.render(view: "/verify/emailTemplates/${emailVerification.action}",
                model: [serverUrl: serverUrl, verificationCode: verificationCode])
        JSON payload = [email: emailVerification.email, subject: 'Tempvs', body: body] as JSON
        String emailServiceUrl = EMAIL_SERVICE_URL + SEND_EMAIL_API_URI
        Map<String, String> headers = [token: EMAIL_SECURITY_TOKEN.encodeAsMD5() as String]
        restCallService.doPost(emailServiceUrl, payload, headers)
    }

    Boolean isEmailUnique(String email, String action, Long instanceId) {
        User user = userService.getUserByEmail(email)
        UserProfile userProfile = profileService.getProfileByProfileEmail(UserProfile, email)
        ClubProfile clubProfile = profileService.getProfileByProfileEmail(ClubProfile, email)

        switch (action) {
            case REGISTRATION_ACTION:
                return !(user || userProfile || clubProfile)
                break
            case EMAIL_ACTION:
                if (user) {
                    return Boolean.FALSE
                } else if (userProfile && (userProfile.user.id != instanceId)) {
                    return Boolean.FALSE
                } else if (clubProfile && (clubProfile.user.id != instanceId)) {
                    return Boolean.FALSE
                }

                break
            case USER_PROFILE_ACTION:
                if (userProfile) {
                    return Boolean.FALSE
                } else if (user && user.userProfile.id != instanceId) {
                    return Boolean.FALSE
                } else if (clubProfile && clubProfile.user.userProfile.id != instanceId) {
                    return Boolean.FALSE
                }

                break
            case CLUB_PROFILE_ACTION:
                if (clubProfile) {
                    return Boolean.FALSE
                } else if (user && !user.clubProfiles.find{it.id == instanceId}) {
                    return Boolean.FALSE
                } else if (userProfile && !userProfile.user.clubProfiles.find{it.id == instanceId}) {
                    return Boolean.FALSE
                }

                break
            default:
                throw new IllegalArgumentException("Unsupported action!")
                break
        }

        return Boolean.TRUE
    }
}
