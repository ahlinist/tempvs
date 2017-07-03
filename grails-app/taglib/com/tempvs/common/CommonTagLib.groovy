package com.tempvs.common

/**
 * General features taglib.
 */
class CommonTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def modalButton = { Map attrs, Closure body ->
        out << render(template: '/templates/common/modalButton', model: attrs + [body: body()])
    }

    def formField = { Map attrs ->
        out << render(template: '/templates/common/formField', model: attrs)
    }
}
