package com.tempvs.ajax

import com.tempvs.user.User
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(AjaxResponseService)
class AjaxResponseServiceSpec extends Specification {
    private static final String SUCCESS_MESSAGE = 'Success Message'
    private static final String DEFAULT_SUCCESS_MESSAGE = 'Success'
    private static final String FAILED = 'failed'

    def user = Mock(User)
    def messageSource = Mock(MessageSource)
    def errors = Mock(Errors)
    def fieldError = Mock(FieldError)

    def setup() {
        service.messageSource = messageSource
    }

    def cleanup() {
    }

    void "Check successful composeJsonResponse() for 2 args"() {
        when: 'composeJsonResponse() called for 2 args'
        def result = service.composeJsonResponse(user, SUCCESS_MESSAGE)

        then: 'Ajax response returned'
        1 * user.hasErrors() >> Boolean.FALSE
        1 * messageSource.getMessage(SUCCESS_MESSAGE, null, DEFAULT_SUCCESS_MESSAGE, LocaleContextHolder.locale) >> SUCCESS_MESSAGE
        0 * _
        result.target.success == Boolean.TRUE
        result.target.messages == [SUCCESS_MESSAGE] as Set
        result instanceof JSON
    }

    void "Check failed composeJsonResponse() for 1 arg"() {
        when: 'composeJsonResponse() called for 1 arg'
        def result = service.composeJsonResponse(user)

        then: 'Ajax response returned'
        1 * user.hasErrors() >> Boolean.TRUE
        1 * user.errors >> errors
        1 * errors.allErrors >> [fieldError, fieldError]
        2 * messageSource.getMessage(fieldError, LocaleContextHolder.locale) >> FAILED
        0 * _
        result.target.success == Boolean.FALSE
        result.target.messages == [FAILED, FAILED] as Set
        result instanceof JSON
    }

    void "Check renderMessage() method"() {
        when: 'renderMessage() called'
        def result = service.renderMessage(Boolean.TRUE, SUCCESS_MESSAGE)

        then: 'Ajax response returned'
        1 * messageSource.getMessage(SUCCESS_MESSAGE, null, DEFAULT_SUCCESS_MESSAGE, LocaleContextHolder.locale) >> SUCCESS_MESSAGE
        0 * _
        result.target.success == Boolean.TRUE
        result.target.messages == [SUCCESS_MESSAGE]
        result instanceof JSON
    }
}
