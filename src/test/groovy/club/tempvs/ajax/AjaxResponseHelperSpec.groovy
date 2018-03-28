package club.tempvs.ajax

import club.tempvs.user.User
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import spock.lang.Specification

/**
 * A unit-test suite for {@link club.tempvs.ajax.AjaxResponseHelper}.
 */
class AjaxResponseHelperSpec extends Specification {

    private static final String FIELD = 'field'
    private static final String TEST_URI = '/test/uri'
    private static final String REDIRECT_ACTION = 'redirect'
    private static final String FORM_MESSAGE = 'formMessage'
    private static final String FAIL_MESSAGE = 'Fail Message'
    private static final String SUCCESS_MESSAGE = 'Success Message'

    def user = Mock(User)
    def validationTagLib = Mock(ValidationTagLib)
    def errors = Mock(Errors)
    def fieldError = Mock(FieldError)

    AjaxResponseHelper ajaxResponseHelper

    def setup() {
        ajaxResponseHelper = new AjaxResponseHelper()
        ajaxResponseHelper.validationTagLib = validationTagLib
    }

    def cleanup() {
    }

    void "Check successful renderValidationResponse() for 2 args"() {
        given:
        Map messageMap = [code: SUCCESS_MESSAGE]

        when: 'renderValidationResponse() called for 2 args'
        def result = ajaxResponseHelper.renderValidationResponse(user, SUCCESS_MESSAGE)

        then:
        1 * user.hasErrors() >> Boolean.FALSE
        1 * validationTagLib.message(messageMap) >> SUCCESS_MESSAGE
        0 * _

        and:
        result.target == [action: FORM_MESSAGE, message: SUCCESS_MESSAGE, success: Boolean.TRUE]
    }

    void "Check failed renderValidationResponse() for 1 arg"() {
        given:
        Map errorMap = [error: fieldError]

        when: 'renderValidationResponse() called for 1 arg'
        def result = ajaxResponseHelper.renderValidationResponse(user)

        then:
        1 * user.hasErrors() >> Boolean.TRUE
        1 * user.errors >> errors
        1 * errors.allErrors >> [fieldError, fieldError]
        2 * validationTagLib.message(errorMap) >> FAIL_MESSAGE
        2 * fieldError.field >> FIELD
        0 * _

        and:
        result.target.size() == 2
    }

    void "Check renderFormMessage() method"() {
        given:
        Map messageMap = [code: SUCCESS_MESSAGE]

        when: 'renderMessage() called'
        def result = ajaxResponseHelper.renderFormMessage(Boolean.TRUE, SUCCESS_MESSAGE)

        then: 'Ajax response returned'
        1 * validationTagLib.message(messageMap) >> SUCCESS_MESSAGE
        0 * _

        and:
        result.target == [action: FORM_MESSAGE, success: Boolean.TRUE, message: SUCCESS_MESSAGE]
    }

    void "Test renderRedirect()"() {
        when:
        def result = ajaxResponseHelper.renderRedirect(TEST_URI)

        then:
        result.target.action == REDIRECT_ACTION
        result.target.location == TEST_URI
    }
}
