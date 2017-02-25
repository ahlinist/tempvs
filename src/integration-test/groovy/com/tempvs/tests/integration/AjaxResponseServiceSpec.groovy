package com.tempvs.tests.integration

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import com.tempvs.tests.utils.TestingUtils
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification

@Integration
@Rollback
@Mock([User, UserProfile, EmailVerification])
class AjaxResponseServiceSpec extends Specification {
    def ajaxResponseService
    def messageSource

    private static final String SUCCESS_MESSAGE = 'user.register.verification.sent.message'

    def setup() {
    }

    def cleanup() {
    }

    void "Check composeJsonResponse() for correct instance"() {
        when:'Invoking composeJsonResponse() for correct emailVerification'
        def result = ajaxResponseService.composeJsonResponse(TestingUtils.createEmailVerification(), SUCCESS_MESSAGE)

        then: 'Successful JSON is returned'
        result.target.success == successfulJson.target.success
        result.target.messages as Set == successfulJson.target.messages as Set
    }

    void "Check composeJsonResponse() for incorrect instance"() {
        given: 'Creating incorrect notification'
        EmailVerification emailVerification = new EmailVerification()
        emailVerification.validate()

        expect: 'Invoking composeJsonResponse() for incorrect emailVerification returns failed JSON'
        ajaxResponseService.composeJsonResponse(emailVerification, SUCCESS_MESSAGE).target.success == Boolean.FALSE
    }

    private JSON getSuccessfulJson() {
        String message = messageSource.getMessage(SUCCESS_MESSAGE, null, LocaleContextHolder.locale)
        [success: Boolean.TRUE, messages: [message]] as JSON
    }
}
