package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
import grails.compiler.GrailsCompileStatic
import grails.plugins.mail.MailService

/**
 *
 */
@GrailsCompileStatic
class VerifyService {

    MailService mailService
    ObjectDAOService objectDAOService

    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
    }

    EmailVerification createEmailVerification(EmailVerification emailVerification) {
        String verificationCode = (emailVerification.email + new Date().time).encodeAsMD5()
        emailVerification.verificationCode = verificationCode
        objectDAOService.save(emailVerification)
    }

    void sendEmailVerification(EmailVerification emailVerification) {
        mailService.sendMail {
            to emailVerification.email
            from 'no-reply@tempvs.com'
            subject 'Tempvs'
            body(view: "/verify/emailTemplates/${emailVerification.action}", model: emailVerification.properties)
        }
    }
}
