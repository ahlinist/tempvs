package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator

/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@GrailsCompileStatic
class SourceController {

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
        if (command.validate()) {
            Period period = Period.valueOf(request.getHeader('referer').tokenize('/').last().toUpperCase())
            Map properties = command.properties + [period: period]
            List<Image> images = imageService.uploadImages(properties.imageBeans as List<ImageUploadBean>, SOURCE_COLLECTION)
            Source source = sourceService.createSource(properties as Source, images)

            if (source.validate()) {
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: source.id))
            } else {
                render ajaxResponseService.renderValidationResponse(source)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def editSource(SourceCommand command) {
        if (command.validate()) {
            String sourceId = request.getHeader('referer').tokenize('/').last()
            Source source = sourceService.getSource sourceId

            if (source) {
                Source editedSource = sourceService.editSource(source, command.properties)

                if (editedSource.validate()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: source.id))
                } else {
                    render ajaxResponseService.renderValidationResponse(editedSource)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SOURCE_FOUND)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
