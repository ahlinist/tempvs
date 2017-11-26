package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
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
    private static final String SUCCESS_ACTION = 'success'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'

    static allowedMethods = [
            index: 'GET',
            period: 'GET',
            getSourcesByPeriod: 'GET',
            show: 'GET',
            createSource: 'POST',
            editSourceField: 'POST'
    ]

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
        Period period = id ? Period.valueOf(id.toUpperCase()) : null
        List<Source> sources = sourceService.getSourcesByPeriod(period)
        render(sources.collect { Source source -> [id: source.id, name: source.name]} as JSON)
    }

    def show(String id) {
        if (id) {
            Source source = sourceService.getSource(id)
            [source: source, period: source?.period, editAllowed: Boolean.TRUE]
        }
    }

    def createSource(SourceCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseService.renderValidationResponse(command))
        }

        List<Image> images = imageService.uploadImages(command.imageUploadBeans, SOURCE_COLLECTION)
        Source source = sourceService.updateSource(command.properties as Source, images)

        if (source.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(source))
        }

        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
    }

    def editSourceField() {
        Source source = sourceService.getSource params.objectId

        if (source) {
            String fieldName = params.fieldName
            String fieldValue = params.fieldValue
            source = sourceService.editSourceField(source, fieldName, fieldValue)

            if (source.hasErrors()) {
                render ajaxResponseService.renderValidationResponse(source)
            } else {
                render([action: SUCCESS_ACTION] as JSON)
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE)
        }
    }
}
