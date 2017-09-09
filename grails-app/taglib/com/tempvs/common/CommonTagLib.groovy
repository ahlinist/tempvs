package com.tempvs.common

/**
 * General features taglib.
 */
class CommonTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def modalButton = { Map attrs, Closure body ->
        out << render(template: '/common/templates/modalButton', model: attrs + [body: body()])
    }

    def formField = { Map attrs ->
        out << render(template: '/common/templates/formField', model: attrs)
    }
}
