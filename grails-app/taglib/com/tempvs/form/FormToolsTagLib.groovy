package com.tempvs.form

/**
 * Html-form functionality handling taglib.
 */
class FormToolsTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def ajaxForm = { Map attrs, Closure body ->
        out << render(template: '/templates/form/ajax/ajaxForm', model: attrs + [body: body()])
    }

    def formField = { Map attrs ->
        out << render(template: '/templates/form/formField', model: attrs)
    }

    def ajaxSubmitButton = { Map attrs ->
        out << render(template: '/templates/form/ajax/submitButton', model: attrs)
    }
}
