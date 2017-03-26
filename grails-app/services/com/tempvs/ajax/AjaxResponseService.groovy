package com.tempvs.ajax

import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder

@Transactional
class AjaxResponseService {
    def messageSource

    private static final String DEFAULT_SUCCESS_MESSAGE = 'Success'
    private static final String DEFAULT_FAIL_MESSAGE = 'Fail'

    JSON composeJsonResponse(instance, String successMessage = null) {
        Boolean success = Boolean.TRUE
        Set messages = []

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
        Set messages = [messageSource.getMessage(message, null, defaultMessage, LocaleContextHolder.locale)]
        [success: success, messages: messages] as JSON
    }
}
