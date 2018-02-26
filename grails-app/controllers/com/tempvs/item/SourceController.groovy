package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.Comment
import com.tempvs.communication.CommentService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.image.ImageUploadCommand
import com.tempvs.periodization.Period
import com.tempvs.user.Profile
import com.tempvs.user.UserInfoHelper
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

/**
 * Controller for {@link com.tempvs.item.Source} entities managing.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class SourceController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String REPLACE_ACTION = 'replaceElement'
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
            addComment: 'POST',
            deleteComment: 'DELETE',
    ]

    ImageService imageService
    SourceService sourceService
    UserInfoHelper userInfoHelper
    CommentService commentService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    @Secured('permitAll')
    def index() {
        [availablePeriods: Period.values()]
    }

    @Secured('permitAll')
    Map period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)

            [
                    sources: sources,
                    period: period,
                    availableTypes: Type.values(),
            ]
        }
    }

    def getSourcesByPeriod(String id) {
        Period period = id ? Period.valueOf(id.toUpperCase()) : null
        List<Source> sources = sourceService.getSourcesByPeriod(period)
        render(sources.collect { Source source -> [id: source.id, name: source.name]} as JSON)
    }

    @Secured('permitAll')
    def show(Long id) {
        if (id) {
            Source source = sourceService.getSource id
            [source: source, period: source?.period, images: source?.images]
        }
    }

    def createSource(Source source, ImageUploadCommand command) {
        List<ImageUploadBean> imageUploadBeans = command.imageUploadBeans

        if (!imageUploadBeans.every { it.validate() }) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBeans.find { it.hasErrors() }))
        }

        if (!source.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        source.images = imageService.uploadImages(imageUploadBeans, SOURCE_COLLECTION)
        source = sourceService.saveSource(source)

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

        Map model = [images: source.images, sourceId: sourceId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/source/templates/imageSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteSource(Long id) {
        Source source = sourceService.getSource id
        Period period = source.period

        if (!source) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        sourceService.deleteSource source
        List<Source> sources = sourceService.getSourcesByPeriod(period)
        Map model = [sources: sources, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/source/templates/sourceList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addImage(ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        Source source = sourceService.getSource params.sourceId as Long
        Image image = imageService.uploadImage(imageUploadBean, SOURCE_COLLECTION)

        if (!source || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        source.addToImages(image)
        source = sourceService.saveSource(source)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        Map model = [images: source.images, sourceId: params.sourceId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/source/templates/imageSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addComment(Long sourceId, String text) {
        Source source = sourceService.getSource sourceId

        if (!source || !text) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)
        Comment comment = commentService.createComment(text, profile)
        source = sourceService.addComment(source, comment)

        if (comment.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(comment)
        }

        Map model = [source: source, currentProfile: profile]
        String template = groovyPageRenderer.render(template: '/source/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteComment(Long objectId, Long commentId) {
        Source source = sourceService.getSource objectId
        Comment comment = commentService.getComment commentId

        if (!source || !comment) {
            return render([action: NO_ACTION] as JSON)
        }

        source = sourceService.deleteComment(source, comment)

        if (source.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(source)
        }

        Map model = [source: source]
        String template = groovyPageRenderer.render(template: '/source/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }
}
