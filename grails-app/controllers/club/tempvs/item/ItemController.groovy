package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

/**
 * Controller that manages operations with {@link Item}.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class ItemController {

    private static final String NO_ACTION = 'none'
    private static final String ITEM_COLLECTION = 'item'
    private static final String SUCCESS_ACTION = 'success'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'

    static defaultAction = 'stash'

    static allowedMethods = [
            show: 'GET',
            deleteItem: 'DELETE',
            editItemField: 'POST',
            addImages: 'POST',
            deleteImage: 'DELETE',
            addSource: 'POST',
            deleteSource: 'DELETE',
            addComment: 'POST',
            deleteComment: 'DELETE',
    ]

    ItemService itemService
    UserService userService
    ImageService imageService
    SourceService sourceService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def deleteImage(Long objectId, Long imageId) {
        Item item = itemService.getItem objectId
        Image image = imageService.getImage imageId

        if (!item) {
            return render([action: NO_ACTION] as JSON)
        }

        item = itemService.deleteImage(item, image)

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        Map model = [
                images: item.images,
                objectId: objectId,
                controllerName: 'item',
                addingAllowed: Boolean.TRUE,
                deletingAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    @Secured('permitAll')
    def show(Long id) {
        if (id) {
            Item item = itemService.getItem id

            if (item) {
                ItemGroup itemGroup = item.itemGroup
                User user = itemGroup.user
                List<Source> sources = item.sources
                List<Source> availableSources = sourceService.getSourcesByPeriodAndItemType(item.period, item.itemType, sources)

                [
                        user: user,
                        item: item,
                        itemGroup: itemGroup,
                        profile: user.userProfile,
                        editAllowed: user.id == userService.currentUserId,
                        images: item.images.sort {it.id},
                        sourceMap: composeSourceMap(sources),
                        sources: availableSources,
                ]
            }
        }
    }

    def deleteItem(Long id) {
        Item item = itemService.getItem id

        if (!item) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE))
        }

        itemService.deleteItem item
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'item', action: 'group', id: item.itemGroup.id))
    }

    def addImages(ImageUploadCommand command) {
        List<ImageUploadBean> imageUploadBeans = command.imageUploadBeans

        if (!imageUploadBeans || imageUploadBeans.every { it.image.empty }) {
            return render([action: NO_ACTION] as JSON)
        }

        if (!imageUploadBeans.every { it.validate() }) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBeans.find { it.hasErrors() }))
        }

        Item item = itemService.getItem(params.objectId as Long)
        List<Image> images = imageService.uploadImages(imageUploadBeans, ITEM_COLLECTION)

        if (!item || !images) {
            return render([action: NO_ACTION] as JSON)
        }

        images.each { Image image -> item.addToImages(image) }
        item = itemService.saveItem(item)

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        Map model = [
                images: item.images,
                objectId: params.objectId,
                controllerName: 'item',
                addingAllowed: Boolean.TRUE,
                deletingAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def editItemField(Long objectId, String fieldName, String fieldValue) {
        Item item = itemService.getItem objectId

        if (!item) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        item = itemService.editItemField(item, fieldName, fieldValue)

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def linkSource(Long itemId, Long sourceId) {
        Item item = itemService.getItem itemId
        Source source = sourceService.loadSource sourceId

        if (!item) {
            return render([action: NO_ACTION] as JSON)
        }

        Item2Source item2Source = itemService.linkSource(item, source)

        if (item2Source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2Source))
        }

        List<Source> sources = item.sources

        Map model = [
                sourceMap: composeSourceMap(sources),
                itemId: itemId,
                editAllowed: Boolean.TRUE,
                sources: sourceService.getSourcesByPeriodAndItemType(item.period, item.itemType, sources),
        ]

        String template = groovyPageRenderer.render(template: '/item/templates/linkedSources', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def unlinkSource(Long itemId, Long sourceId) {
        Item item = itemService.getItem itemId
        Source source = sourceService.loadSource sourceId

        if (!item) {
            return render([action: NO_ACTION] as JSON)
        }

        itemService.unlinkSource(item, source)
        List<Source> sources = item.sources

        Map model = [
                sourceMap: composeSourceMap(sources),
                itemId: itemId,
                editAllowed: Boolean.TRUE,
                sources: sourceService.getSourcesByPeriodAndItemType(item.period, item.itemType, sources),
        ]

        String template = groovyPageRenderer.render(template: '/item/templates/linkedSources', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }

    private Map<SourceType, List<Source>> composeSourceMap(List<Source> sources) {
        sources.groupBy { Source source ->
            source.sourceType
        }
    }
}
