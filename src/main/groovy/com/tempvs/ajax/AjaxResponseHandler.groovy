package com.tempvs.ajax

import grails.converters.JSON
import grails.util.Holders

class AjaxResponseHandler {
    private static def g = Holders.grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
    Boolean success = Boolean.TRUE
    Set messages = []

    JSON composeJson(instance, String successMessage = null) {
        if (instance.hasErrors()) {
            this.success = Boolean.FALSE

            g.eachError(bean: instance) {
                this.messages << g.message(error: it)
            }
        } else {
            this.messages = [g.message(code: successMessage)]
        }

        this as JSON
    }
}