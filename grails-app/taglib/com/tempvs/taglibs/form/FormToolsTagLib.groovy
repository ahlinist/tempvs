package com.tempvs.taglibs.form

class FormToolsTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def formField = {attrs ->
        out << render(template: '/templates/form/formField', model: attrs)
    }

    def ajaxSubmitButton = { attrs ->
        out << render(template: '/templates/form/ajax/submitButton', model: attrs)
    }
}
