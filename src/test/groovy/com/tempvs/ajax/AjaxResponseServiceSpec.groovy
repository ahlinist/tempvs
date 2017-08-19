package com.tempvs.ajax

import com.tempvs.user.User
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AjaxResponseService)
class AjaxResponseServiceSpec extends Specification {

    private static final String FIELD = 'field'
    private static final String TEST_URI = '/test/uri'
    private static final String FAIL_MESSAGE = 'Fail Message'
    private static final String SUCCESS_MESSAGE = 'Success Message'

    def user = Mock(User)
    def validationTagLib = Mock(ValidationTagLib)
    def errors = Mock(Errors)
    def fieldError = Mock(FieldError)

    def setup() {
        service.validationTagLib = validationTagLib
    }

    def cleanup() {
    }

    void "Check successful renderValidationResponse() for 2 args"() {
        given:
        Map messageMap = [code: SUCCESS_MESSAGE]

        when: 'renderValidationResponse() called for 2 args'
        def result = service.renderValidationResponse(user, SUCCESS_MESSAGE)

        then:
        1 * user.hasErrors() >> Boolean.FALSE
        1 * validationTagLib.message(messageMap) >> SUCCESS_MESSAGE
        0 * _

        and:
        result.target == [formMessage: Boolean.TRUE, message: SUCCESS_MESSAGE, success: Boolean.TRUE]
        result instanceof JSON
    }

    void "Check failed renderValidationResponse() for 1 arg"() {
        given:
        Map errorMap = [error: fieldError]

        when: 'renderValidationResponse() called for 1 arg'
        def result = service.renderValidationResponse(user)

        then:
        1 * user.hasErrors() >> Boolean.TRUE
        1 * user.errors >> errors
        1 * errors.allErrors >> [fieldError, fieldError]
        2 * validationTagLib.message(errorMap) >> FAIL_MESSAGE
        2 * fieldError.field >> FIELD
        0 * _

        and:
        result.target.size() == 2
        result instanceof JSON
    }

    void "Check renderFormMessage() method"() {
        given:
        Map messageMap = [code: SUCCESS_MESSAGE]

        when: 'renderMessage() called'
        def result = service.renderFormMessage(Boolean.TRUE, SUCCESS_MESSAGE)

        then: 'Ajax response returned'
        1 * validationTagLib.message(messageMap) >> SUCCESS_MESSAGE
        0 * _

        and:
        result.target == [formMessage: Boolean.TRUE, success: Boolean.TRUE, message: SUCCESS_MESSAGE]
        result instanceof JSON
    }

    void "Test renderRedirect()"() {
        when:
        def result = service.renderRedirect(TEST_URI)

        then:
        result.target.redirect == TEST_URI

        and:
        result instanceof JSON
    }
}
