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
        String email = properties.email
        String verificationCode = email + new Date().time
        EmailVerification emailVerification = new EmailVerification(properties + [verificationCode: verificationCode.encodeAsMD5()])

        if (emailVerification.save(flush: true)) {
            mailService.sendMail {
                to properties.email
                from 'no-reply@tempvs.com'
                subject 'Tempvs'
                body(view: "/verify/emailTemplates/${properties.action}", model: emailVerification.properties)
            }
        }

        emailVerification
    }
}
