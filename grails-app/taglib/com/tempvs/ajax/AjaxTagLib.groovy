package com.tempvs.ajax

/**
 * Html-form functionality handling taglib.
 */
class AjaxTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def ajaxForm = { Map attrs, Closure body ->
        out << render(template: '/templates/ajax/ajaxForm', model: attrs + [body: body()])
    }

    def ajaxLink = { Map attrs ->
        out << render(template: '/templates/ajax/ajaxLink', model: attrs)
    }

    def ajaxSubmitButton = { Map attrs ->
        out << render(template: '/templates/ajax/submitButton', model: attrs)
    }
}
