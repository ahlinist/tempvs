package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@GrailsCompileStatic
class SourceController {

    private static final String REFERER = 'referer'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String NO_SOURCE_FOUND = 'source.notFound.message'

    static allowedMethods = [index: 'GET', period: 'GET', getSourcesByPeriod: 'GET', show: 'GET', createSource: 'POST', editSource: 'POST']

    ImageService imageService
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
        processSource(command) {
            command.properties as Source
        }
    }

    def editSource(SourceCommand command) {
        processSource(command) {
            Source source = sourceService.getSource params.sourceId

            if (source) {
                InvokerHelper.setProperties(source, command.properties)
                source
            }
        }
    }

    private processSource(SourceCommand command, Closure generateSource) {
        if (command.validate()) {
            Source source = generateSource() as Source

            if (source) {
                if (source.validate()) {
                    List<Image> images = imageService.uploadImages(command.imageUploadBeans as List<ImageUploadBean>, SOURCE_COLLECTION)
                    sourceService.saveSource(source, images)
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
