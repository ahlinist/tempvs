package com.tempvs.form

class FormToolsTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def ajaxForm = { attrs, body ->
        out << render(template: '/templates/form/ajax/ajaxForm', model: attrs + [body: body()])
    }

    def formField = { attrs ->
        out << render(template: '/templates/form/formField', model: attrs)
    }

    def ajaxSubmitButton = { attrs ->
        out << render(template: '/templates/form/ajax/submitButton', model: attrs)
    }
}
