package com.tempvs.ajax

import grails.converters.JSON
import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.CompileStatic
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

@Transactional
@CompileStatic
class AjaxResponseService {

    private static final String DEFAULT_SUCCESS_MESSAGE = 'Success'
    private static final String DEFAULT_FAIL_MESSAGE = 'Fail'

    MessageSource messageSource

    JSON composeJsonResponse(Validateable instance, String successMessage = null) {
        Boolean success = Boolean.TRUE
        Set<String> messages = []

        if (instance.hasErrors()) {
            success = Boolean.FALSE

            instance.errors.allErrors.each { error ->
                messages << messageSource.getMessage(error, LocaleContextHolder.locale)
            }
        } else {
            messages << messageSource.getMessage(successMessage, null, DEFAULT_SUCCESS_MESSAGE, LocaleContextHolder.locale)
        }

        [success: success, messages: messages] as JSON
    }

    JSON renderMessage(Boolean success, String message) {
        String defaultMessage = success ? DEFAULT_SUCCESS_MESSAGE : DEFAULT_FAIL_MESSAGE
        [success: success, messages: [messageSource.getMessage(message, null, defaultMessage, LocaleContextHolder.locale)]] as JSON
    }
}
