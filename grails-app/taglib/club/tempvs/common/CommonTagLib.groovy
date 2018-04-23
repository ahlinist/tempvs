package club.tempvs.common

/**
 * General features taglib.
 */
class CommonTagLib {

    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'

    def formField = { Map attrs ->
        out << render(template: '/common/templates/formField', model: attrs)
    }
}
