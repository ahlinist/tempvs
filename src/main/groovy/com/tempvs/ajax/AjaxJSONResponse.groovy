package com.tempvs.ajax

import grails.converters.JSON
import grails.util.Holders

class AjaxJSONResponse {
    private static final def g = Holders.grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    Boolean success = Boolean.TRUE
    Set messages = []

    JSON init(instance, String successMessage) {
        this.populateFields(instance, successMessage)
        return this as JSON
    }

    JSON initImage(multiPartFile, instance, String successMessage) {
        if (!multiPartFile?.empty) {
            this.populateFields(instance, successMessage)
        } else {
            this.success = Boolean.FALSE
            this.messages = [g.message(code: IMAGE_EMPTY)]
        }

        return this as JSON
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