package club.tempvs.ajax

/**
 * Html-form functionality handling taglib.
 */
class AjaxTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def ajaxForm = { Map attrs, Closure body ->
        out << render(template: '/ajax/templates/ajaxForm', model: attrs + [body: body()])
    }

    def ajaxSmartForm = { Map attrs, Closure body ->
        out << render(template: '/ajax/templates/ajaxSmartForm', model: attrs + [body: body()])
    }
}
