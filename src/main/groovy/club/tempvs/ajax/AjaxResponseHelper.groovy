package club.tempvs.ajax

import grails.converters.JSON
import grails.validation.Validateable
import groovy.transform.CompileStatic
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

/**
 * A helper for composing responses for ajax requests.
 */
@CompileStatic
class AjaxResponseHelper {

    private static final String REDIRECT = 'redirect'
    private static final String FORM_MESSAGE = 'formMessage'
    private static final String VALIDATION_RESPONSE = 'validationResponse'

    ValidationTagLib validationTagLib

    JSON renderErrors(Validateable instance) {
        instance.errors.allErrors.collect { ObjectError error ->
            [message: validationTagLib.message(error: error), name: ((FieldError) error).field]
        } as JSON
    }

    JSON renderErrors(List<LinkedHashMap> messages) {
        messages.collect { Map messageMap ->
            [message: validationTagLib.message(code: messageMap.message), name: messageMap.name]
        } as JSON
    }

    JSON renderValidationResponse(Validateable instance, String successMessage = null) {
        if (instance.hasErrors()) {
            List result = instance.errors.allErrors.collect { ObjectError error ->
                [message: validationTagLib.message(error: error), name: ((FieldError) error).field]
            }

            [action: VALIDATION_RESPONSE, messages: result] as JSON
        } else {
            renderFormMessage(Boolean.TRUE, successMessage)
        }
    }

    JSON renderValidationResponse(Errors errors) {
        List result = errors.allErrors.collect { ObjectError error ->
            [message: validationTagLib.message(error: error), name: ((FieldError) error).field]
        }

        [action: VALIDATION_RESPONSE, messages: result] as JSON
    }

    JSON renderFormMessage(Boolean success, String code) {
        [action: FORM_MESSAGE, success: success, message: validationTagLib.message(code: code)] as JSON
    }

    JSON renderRedirect(String uri) {
        [action: REDIRECT, location: uri] as JSON
    }
}
