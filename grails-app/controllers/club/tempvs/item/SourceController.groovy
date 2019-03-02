package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.communication.CommentService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import club.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

/**
 * Controller for {@link Source} entities managing.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class SourceController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String SOURCE_COLLECTION = 'source'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'
    private static final String ROLE_SCRIBE = 'ROLE_SCRIBE'
    private static final String ROLE_CONTRIBUTOR = 'ROLE_CONTRIBUTOR'

    static allowedMethods = [
            index: 'GET',
            getSourcesByPeriod: 'GET',
            show: 'GET',
            editSourceField: 'POST',
            deleteImage: 'DELETE',
            deleteSource: 'DELETE',
            addComment: 'POST',
            addImages: 'POST',
            deleteComment: 'DELETE',
    ]


    UserService userService
    ImageService imageService
    SourceService sourceService
    CommentService commentService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    @Secured('permitAll')
    def index() {
        redirect controller: 'library'
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

            [
                    source: source,
                    period: source?.period,
                    images: source?.images,
                    editAllowed: userService.currentUserId != null,
            ]
        }
    }

    @Secured("hasRole('ROLE_SCRIBE')")
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

    @Secured("hasRole('ROLE_SCRIBE')")
    def deleteImage(Long objectId, Long imageId) {
        Source source = sourceService.getSource objectId
        Image image = imageService.getImage imageId

        if (!source) {
            return render([action: NO_ACTION] as JSON)
        }

        source = sourceService.deleteImage(source, image)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        Map model = [
                images: source.images,
                objectId: objectId,
                controllerName: 'source',
                addingAllowed: userService.ifAnyRoleGranted(ROLE_CONTRIBUTOR),
                deletingAllowed: userService.ifAnyRoleGranted(ROLE_SCRIBE),
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    @Secured("hasRole('ROLE_ARCHIVARIUS')")
    def deleteSource(Long id) {
        Source source = sourceService.getSource id

        if (!source) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        sourceService.deleteSource source
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'library', action: 'period', id: source.period.id))
    }

    @Secured("hasRole('ROLE_CONTRIBUTOR')")
    def addImages(ImageUploadCommand command) {
        List<ImageUploadBean> imageUploadBeans = command.imageUploadBeans

        if (!imageUploadBeans || imageUploadBeans.every { it.image.empty }) {
            return render([action: NO_ACTION] as JSON)
        }

        if (!imageUploadBeans.every { it.validate() }) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBeans.find { it.hasErrors() }))
        }

        Source source = sourceService.getSource params.objectId as Long
        List<Image> images = imageService.uploadImages(imageUploadBeans, SOURCE_COLLECTION)

        if (!source || !images) {
            return render([action: NO_ACTION] as JSON)
        }

        images.each { Image image -> source.addToImages(image)}
        source = sourceService.saveSource(source)

        if (source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(source))
        }

        Map model = [
                images: source.images,
                objectId: params.objectId,
                controllerName: 'source',
                addingAllowed: userService.ifAnyRoleGranted(ROLE_CONTRIBUTOR),
                deletingAllowed: userService.ifAnyRoleGranted(ROLE_SCRIBE),
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addComment(Long objectId, String text) {
        Source source = sourceService.getSource objectId

        if (!source || !text) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = userService.currentProfile
        Comment comment = commentService.createComment(text, profile)
        source = sourceService.addComment(source, comment)

        if (comment.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(comment)
        }

        Map model = [
                object: source,
                objectId: objectId,
                controllerName: 'source',
                editAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/communication/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteComment(Long objectId, Long commentId) {
        Source source = sourceService.getSource objectId
        Comment comment = commentService.loadComment commentId

        if (!source) {
            return render([action: NO_ACTION] as JSON)
        }

        source = sourceService.deleteComment(source, comment)

        if (source.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(source)
        }

        Map model = [
                object: source,
                objectId: objectId,
                controllerName: 'source',
                editAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/communication/templates/comments', model: model)
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
