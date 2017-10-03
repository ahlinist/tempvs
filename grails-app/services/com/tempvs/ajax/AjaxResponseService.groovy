package com.tempvs.ajax

import grails.converters.JSON
import grails.validation.Validateable
import groovy.transform.CompileStatic
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

/**
 * A service for composing responses for ajax requests.
 */
@CompileStatic
class AjaxResponseService {

    ValidationTagLib validationTagLib

    JSON renderValidationResponse(Validateable instance, String successMessage = null) {
        if (instance.hasErrors()) {
            List result = instance.errors.allErrors.collect { ObjectError error ->
                [message: validationTagLib.message(error: error), name: ((FieldError) error).field]
            }

            result as JSON
        } else {
            renderFormMessage(Boolean.TRUE, successMessage)
        }
    }

    JSON renderFormMessage(Boolean success, String code) {
        [formMessage: Boolean.TRUE, success: success, message: validationTagLib.message(code: code)] as JSON
    }

    JSON renderRedirect(String uri) {
        [redirect: uri] as JSON
    }
}
