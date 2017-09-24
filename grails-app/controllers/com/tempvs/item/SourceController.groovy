package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator

/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@GrailsCompileStatic
class SourceController {

    private static final String REFERER = 'referer'
    private static final String NO_SOURCE_FOUND = 'source.notFound.message'

    static allowedMethods = [index: 'GET', period: 'GET', getSourcesByPeriod: 'GET', show: 'GET', createSource: 'POST', editSource: 'POST']

    SourceService sourceService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService

    def index() {

    }

    Map period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)
            [sources: sources, period: period, editAllowed: Boolean.TRUE]
        }
    }

    def getSourcesByPeriod(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)
            render((sources?.collect { Source source -> [id: source.id, name: source.name]} ?: []) as JSON)
        }
    }

    def show(String id) {
        if (id) {
            Source source = sourceService.getSource(id)
            [source: source, period: source?.period, editAllowed: Boolean.TRUE]
        }
    }

    def createSource(SourceCommand command) {
        Closure sourceClosure = {
            command.properties as Source
        }

        processRequest(command, sourceClosure) { Source source ->
            sourceService.createSource(source, command.imageUploadBeans)
        }
    }

    def editSource(SourceCommand command) {
        Closure sourceClosure = {
            sourceService.getSource params.sourceId
        }

        processRequest(command, sourceClosure) { Source source ->
            sourceService.editSource(source, command.properties)
        }
    }

    private processRequest(SourceCommand command, Closure<Source> sourceClosure, Closure<Source> action) {
        if (command.validate()) {
            Source source = sourceClosure()

            if (source) {
                if (action(source).validate()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(source)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SOURCE_FOUND)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
