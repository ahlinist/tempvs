package com.tempvs.services

import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder

@Transactional
class AjaxResponseService {
    def messageSource

    private static final String DEFAULT_SUCCESS_MESSAGE = 'Success'

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
}
