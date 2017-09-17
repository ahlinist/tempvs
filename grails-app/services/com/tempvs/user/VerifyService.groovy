package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugins.mail.MailService
import grails.transaction.Transactional

@Transactional
@GrailsCompileStatic
class VerifyService {

    MailService mailService

    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
    }

    EmailVerification createEmailVerification(Map properties) {
        properties += [verificationCode: (properties.email.toString() + new Date().time).encodeAsMD5()]
        EmailVerification emailVerification = properties as EmailVerification
        emailVerification.save()
        emailVerification
    }

    Boolean sendEmailVerification(EmailVerification emailVerification) {
        mailService.sendMail {
            to emailVerification.email
            from 'no-reply@tempvs.com'
            subject 'Tempvs'
            body(view: "/verify/emailTemplates/${emailVerification.action}", model: emailVerification.properties)
        }
    }
}
