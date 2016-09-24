package com.tempvs.ajax

import grails.util.Holders
import org.springframework.web.multipart.MultipartFile

class AjaxResponse {
    private static def g = Holders.grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    Boolean success = Boolean.TRUE
    Set messages = []

    AjaxResponse(instance) {
        this.populateFields(instance, null)
    }

    AjaxResponse(instance, String successMessage) {
        this.populateFields(instance, successMessage)
    }

    AjaxResponse(instance, String successMessage, MultipartFile multiPartFile) {
        if (!multiPartFile?.empty) {
            this.populateFields(instance, successMessage)
        } else {
            this.success = Boolean.FALSE
            this.messages = [g.message(code: IMAGE_EMPTY)]
        }
    }

    private void populateFields(instance, String successMessage) {
        if (!instance.hasErrors()) {
            this.messages = [g.message(code: successMessage)]
        } else {
            this.success = Boolean.FALSE

            g.eachError(bean: instance) {
                this.messages << g.message(error: it)
            }
        }
    }
}