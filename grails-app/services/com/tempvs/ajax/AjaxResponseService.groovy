package com.tempvs.ajax

import grails.converters.JSON
import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.CompileStatic
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

/**
 * A service for composing responses for ajax requests.
 */
@Transactional
@CompileStatic
class AjaxResponseService {

    private static final String DEFAULT_SUCCESS_MESSAGE = 'Success'
    private static final String DEFAULT_FAIL_MESSAGE = 'Fail'

    MessageSource messageSource

    JSON renderValidationResponse(Validateable instance, String successMessage = null) {
        if (instance.hasErrors()) {
            List<Map> result = []

            instance.errors.allErrors.each { ObjectError error ->
                String message = messageSource.getMessage(error, LocaleContextHolder.locale)
                result << [message: message, name: ((FieldError) error).field]
            }

            result as JSON
        } else {
            renderFormMessage(Boolean.TRUE, successMessage)
        }
    }

    JSON renderFormMessage(Boolean success, String code) {
        String defaultMessage = success ? DEFAULT_SUCCESS_MESSAGE : DEFAULT_FAIL_MESSAGE
        String message = messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.locale)
        [formMessage: Boolean.TRUE, success: success, message: message] as JSON
    }

    JSON renderRedirect(String uri) {
        [redirect: uri] as JSON
    }
}
