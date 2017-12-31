package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugins.mail.MailService
import grails.gorm.transactions.Transactional

/**
 * A service that handles operations related to {@link com.tempvs.user.EmailVerification}.
 */
@Transactional
@GrailsCompileStatic
class VerifyService {

    private static String EMAIL_FIELD = 'email'
    private static String EMAIL_ACTION = 'email'
    private static String USER_PROFILE_ACTION = 'userProfile'
    private static String CLUB_PROFILE_ACTION = 'clubProfile'
    private static String REGISTRATION_ACTION = 'registration'
    private static String EMAIL_USED_CODE = 'emailVerification.email.used.error'

    UserService userService
    MailService mailService
    ProfileService profileService

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

    void sendEmailVerification(EmailVerification emailVerification) {
        mailService.sendMail {
            to emailVerification.email
            from 'no-reply@tempvs.com'
            subject 'Tempvs'
            body(view: "/verify/emailTemplates/${emailVerification.action}", model: emailVerification.properties)
        }
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
