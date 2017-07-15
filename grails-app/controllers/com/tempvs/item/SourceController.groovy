package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.web.mapping.LinkGenerator

/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@GrailsCompileStatic
class SourceController {

    SourceService sourceService
    AjaxResponseService ajaxResponseService
    LinkGenerator grailsLinkGenerator

    def index() {

    }

    def period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)
            [sources: sources, period: period, editAllowed: Boolean.TRUE]
        }
    }

    def show(String id) {
        if (id) {
            Source source = sourceService.getSource(id)
            [source: source, period: source.period, editAllowed: Boolean.TRUE]
        }
    }

    def createSource(SourceCommand command) {
        if (command.validate()) {
            Period period = Period.valueOf(request.getHeader('referer').tokenize('/').last().toUpperCase())
            Source source = sourceService.createSource(command.properties + [period: period])

            if (source.validate()) {
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: source.id))
            } else {
                render ajaxResponseService.renderValidationResponse(source)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
