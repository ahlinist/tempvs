package com.tempvs.ajax

class AjaxResponseFactory {
    AjaxResponse newInstance(instance, String successMessage = null) {
        new AjaxResponse(instance, successMessage)
    }
}