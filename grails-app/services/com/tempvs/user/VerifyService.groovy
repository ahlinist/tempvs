package com.tempvs.user

import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.plugins.mail.MailService
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
@GrailsCompileStatic
class VerifyService {

    MailService mailService
    ObjectFactory objectFactory

    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
    }

    EmailVerification createEmailVerification(Map properties) {
        String email = properties.email
        String verificationCode = email + new Date().time
        EmailVerification emailVerification = objectFactory.create(EmailVerification.class)
        Map verificationProperties = properties + [verificationCode: verificationCode.encodeAsMD5()]
        InvokerHelper.setProperties(emailVerification, verificationProperties)

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
