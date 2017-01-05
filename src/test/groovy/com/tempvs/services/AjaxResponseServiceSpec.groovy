package com.tempvs.services

import com.tempvs.domain.user.User
import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(AjaxResponseService)
class AjaxResponseServiceSpec extends Specification {
    private static final SUCCESS_MESSAGE = 'Success Message'
    private static final DEFAULT_SUCCESS_MESSAGE = 'Success'

    def setup() {
    }

    def cleanup() {
    }

    void "Check composeJsonResponse() for 2 args"() {
        given: 'Initial setup'
        def messageSource = Mock(MessageSource)
        service.messageSource = messageSource
        def user = Mock(User)

        when: 'Service returned success for 2 args call'
        def result = service.composeJsonResponse(user, SUCCESS_MESSAGE)

        then: 'AjaxResponseFactory is invoked'
        1 * user.hasErrors()
        1 * messageSource.getMessage(SUCCESS_MESSAGE, null, DEFAULT_SUCCESS_MESSAGE, LocaleContextHolder.locale)
        result.target.success == Boolean.TRUE
    }

    void "Check composeJsonResponse() for 1 arg"() {
        given: 'Initial setup'
        def messageSource = Mock(MessageSource)
        def messageSourceResolvable = Mock(MessageSourceResolvable)
        service.messageSource = messageSource
        def user = [:]
        user.hasErrors = { return Boolean.TRUE }
        user.errors = [allErrors:[messageSourceResolvable, messageSourceResolvable]]

        when: 'Service returned success for 1 arg'
        def result = service.composeJsonResponse(user)

        then: 'AjaxResponseFactory is invoked'
        0 * messageSource.getMessage(SUCCESS_MESSAGE, null, DEFAULT_SUCCESS_MESSAGE, LocaleContextHolder.locale)
        2 * messageSource.getMessage(messageSourceResolvable, LocaleContextHolder.locale)
        result.target.success == Boolean.FALSE
    }
}

