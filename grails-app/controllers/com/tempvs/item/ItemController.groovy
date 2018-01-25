package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.Comment
import com.tempvs.communication.CommentService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.Profile
import com.tempvs.user.ProfileService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String NO_ACTION = 'none'
    private static final String ITEM_COLLECTION = 'item'
    private static final String SUCCESS_ACTION = 'success'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    static defaultAction = 'stash'

    static allowedMethods = [
            stash: 'GET',
            createGroup: 'POST',
            editItemGroupField: 'POST',
            group: 'GET',
            createItem: 'POST',
            show: 'GET',
            deleteItem: 'DELETE',
            deleteGroup: 'DELETE',
            editItemField: 'POST',
            addImage: 'POST',
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
    CommentService commentService
    ProfileService profileService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def stash(Long id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(ItemGroupCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        ItemGroup itemGroup = itemService.createGroup(command.properties as ItemGroup)

        if (itemGroup.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(itemGroup))
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'item', action: 'group', id: itemGroup.id))
    }

    def group(Long id) {
        if (id) {
            ItemGroup itemGroup = itemService.getGroup id

            if (itemGroup) {
                User user = itemGroup.user

                [
                        user: user,
                        itemGroup: itemGroup,
                        items: itemGroup.items.sort { it.id },
                        userProfile: user.userProfile,
                        editAllowed: user.id == userService.currentUserId,
                ]
            }
        }
    }

    def createItem(ItemCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        List<Image> images = imageService.uploadImages(command.imageUploadBeans, ITEM_COLLECTION)
        Item item = itemService.updateItem(command.properties as Item, images)

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'item', action: 'show', id: item.id))
    }

    def deleteImage(Long itemId, Long imageId) {
        Item item = itemService.getItem itemId
        Image image = imageService.getImage imageId

        if (!item || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        item = itemService.deleteImage(item, image)

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        Map model = [images: item.images, itemId: itemId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/item/templates/imageSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def show(Long id) {
        if (id) {
            Item item = itemService.getItem id

            if (item) {
                ItemGroup itemGroup = item.itemGroup
                User user = itemGroup.user

                [
                        user: user,
                        item: item,
                        itemGroup: itemGroup,
                        userProfile: user.userProfile,
                        editAllowed: user.id == userService.currentUserId,
                        images: item.images.sort {it.id},
                        sources: item.sources,
                        availableSources: sourceService.getSourcesByPeriodAndType(item.period, item.type),
                ]
            }
        }
    }

    def deleteItem(Long id) {
        Item item = itemService.getItem id

        if (!item) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE))
        }

        ItemGroup itemGroup = item.itemGroup
        itemService.deleteItem item
        Map model = [items: itemGroup.items, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/item/templates/itemList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteGroup(Long id) {
        ItemGroup itemGroup = itemService.getGroup id

        if (!itemGroup) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE))
        }

        User user = itemGroup.user
        itemService.deleteGroup itemGroup
        Map model = [itemGroups: user.itemGroups, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/item/templates/groupList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addImage(ImageUploadBean imageUploadBean) {
        Item item = itemService.getItem params.itemId as Long
        Image image = imageService.uploadImage(imageUploadBean, ITEM_COLLECTION)

        if (!item || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        item = itemService.updateItem(item, [image])

        if (item.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item))
        }

        Map model = [images: item.images, itemId: params.itemId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/item/templates/imageSection', model: model)
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

    def editItemGroupField(Long objectId, String fieldName, String fieldValue) {
        ItemGroup itemGroup = itemService.getGroup objectId

        if (!itemGroup) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        itemGroup = itemService.editItemGroupField(itemGroup, fieldName, fieldValue)

        if (itemGroup.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(itemGroup))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def linkSource(Long itemId, Long sourceId) {
        Item item = itemService.getItem itemId
        Source source = sourceService.getSource sourceId

        if (!item || !source) {
            return render([action: NO_ACTION] as JSON)
        }

        Item2Source item2Source = itemService.linkSource(item, source)

        if (item2Source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2Source))
        }

        List<Source> availableSources = sourceService.getSourcesByPeriodAndType(item.period, item.type)
        Map model = [sources: item.sources, itemId: itemId, editAllowed: Boolean.TRUE, availableSources: availableSources]
        String template = groovyPageRenderer.render(template: '/item/templates/linkedSources', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def unlinkSource(Long itemId, Long sourceId) {
        Item item = itemService.getItem itemId
        Source source = sourceService.getSource sourceId

        if (!item || !source) {
            return render([action: NO_ACTION] as JSON)
        }

        itemService.unlinkSource(item, source)
        List<Source> availableSources = sourceService.getSourcesByPeriodAndType(item.period, item.type)
        Map model = [sources: item.sources, itemId: itemId, editAllowed: Boolean.TRUE, availableSources: availableSources]
        String template = groovyPageRenderer.render(template: '/item/templates/linkedSources', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addComment(Long itemId, String text) {
        Item item = itemService.getItem itemId

        if (!item || !text) {
            return render([action: NO_ACTION] as JSON)
        }

        Comment comment = commentService.createComment(text)
        item = itemService.addComment(item, comment)

        if (comment.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(comment)
        }

        Map model = [item: item, editAllowed: item.itemGroup.user.id == userService.currentUserId]
        String template = groovyPageRenderer.render(template: '/item/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteComment(Long objectId, Long commentId) {
        Item item = itemService.getItem objectId
        Comment comment = commentService.getComment commentId

        if (!item || !comment) {
            return render([action: NO_ACTION] as JSON)
        }

        item = itemService.deleteComment(item, comment)

        if (item.hasErrors()) {
            ajaxResponseHelper.renderValidationResponse(item)
        }

        Map model = [item: item, editAllowed: item.itemGroup.user.id == userService.currentUserId]
        String template = groovyPageRenderer.render(template: '/item/templates/comments', model: model)
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
