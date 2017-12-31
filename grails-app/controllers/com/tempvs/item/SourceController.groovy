package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@GrailsCompileStatic
class SourceController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String APPEND_ACTION = 'appendElement'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'

    static allowedMethods = [
            index: 'GET',
            period: 'GET',
            getSourcesByPeriod: 'GET',
            show: 'GET',
            createSource: 'POST',
            editSourceField: 'POST',
            deleteImage: 'DELETE',
            deleteSource: 'DELETE',
    ]

    ImageService imageService
    SourceService sourceService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {

    }

    Map period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)
            [sources: sources, period: period]
        }
    }

    def getSourcesByPeriod(String id) {
        Period period = id ? Period.valueOf(id.toUpperCase()) : null
        List<Source> sources = sourceService.getSourcesByPeriod(period)
        render(sources.collect { Source source -> [id: source.id, name: source.name]} as JSON)
    }

    def show(Long id) {
        if (id) {
            Source source = sourceService.getSource id
            [source: source, period: source?.period, images: source?.images]
        }
    }

    def createSource(SourceCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        List<Image> images = imageService.uploadImages(command.imageUploadBeans, SOURCE_COLLECTION)
        Source source = sourceService.updateSource(command.properties as Source, images)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(action: 'show', id: source.id))
    }

    def editSourceField(Long objectId, String fieldName, String fieldValue) {
        Source source = sourceService.getSource objectId

        if (!source) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        source = sourceService.editSourceField(source, fieldName, fieldValue)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def deleteImage(Long sourceId, Long imageId) {
        Source source = sourceService.getSource sourceId
        Image image = imageService.getImage imageId

        if (!source || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        source = sourceService.deleteImage(source, image)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        render([action: DELETE_ACTION] as JSON)
    }

    def deleteSource(Long id) {
        Source source = sourceService.getSource id

        if (!source) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        sourceService.deleteSource source
        render([action: DELETE_ACTION] as JSON)
    }

    def addImage(ImageUploadBean imageUploadBean) {
        Source source = sourceService.getSource params.sourceId as Long
        Image image = imageService.uploadImage(imageUploadBean, SOURCE_COLLECTION)

        if (!source || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        source = sourceService.updateSource(source, [image])

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        Map model = [image: image, itemId: params.sourceId]
        String template = groovyPageRenderer.render(template: '/item/templates/addImageForm', model: model)
        render([action: APPEND_ACTION, template: template] as JSON)
    }
}
