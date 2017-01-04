package com.tempvs.services

import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.context.i18n.LocaleContextHolder

@Transactional
class AjaxResponseService {
    def messageSource

    JSON composeJsonResponse(instance, String successMessage = null) {
        Boolean success = Boolean.TRUE
        Set messages = []

        if (instance.hasErrors()) {
            success = Boolean.FALSE

            instance.errors.allErrors.each {
                messages << messageSource.getMessage(it, LocaleContextHolder.locale)
            }
        } else {
            messages << messageSource.getMessage(successMessage, null, LocaleContextHolder.locale)
        }

        [success: success, messages: messages] as JSON
    }
}
