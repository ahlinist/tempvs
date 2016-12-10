package com.tempvs.services

import com.tempvs.ajax.AjaxResponse
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class AjaxResponseService {
    def ajaxResponseFactory

    JSON composeJsonResponse(instance, String successMessage = null) {
        ajaxResponseFactory.newInstance(instance, successMessage) as JSON
    }
}
