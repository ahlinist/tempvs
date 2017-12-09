package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
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
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String APPEND_ACTION = 'appendElement'
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
    ]

    ItemService itemService
    UserService userService
    ImageService imageService
    SourceService sourceService
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

        render([action: DELETE_ACTION] as JSON)
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
                        sources: itemService.getSourcesByItem(item),
                        availableSources: sourceService.getSourcesByPeriod(item.period),
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
        render([action: DELETE_ACTION] as JSON)
    }

    def deleteGroup(Long id) {
        ItemGroup itemGroup = itemService.getGroup id

        if (!itemGroup) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE))
        }

        itemService.deleteGroup itemGroup
        render([action: DELETE_ACTION] as JSON)
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

        Map model = [image: image, itemId: params.itemId]
        String template = groovyPageRenderer.render(template: '/item/templates/addImageForm', model: model)
        render([action: APPEND_ACTION, template: template] as JSON)
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

        Item2Source item2source = itemService.linkSource(item, source)

        if (item2source.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2source))
        }

        Map model = [editAllowed: Boolean.TRUE, source: source, itemId: itemId]
        String template = groovyPageRenderer.render(template: '/item/templates/linkedSource', model: model)
        render([action: APPEND_ACTION, template: template] as JSON)
    }

    def unlinkSource(Long itemId, Long sourceId) {
        Item item = itemService.getItem itemId
        Source source = sourceService.getSource sourceId

        if (!item || !source) {
            return render([action: NO_ACTION] as JSON)
        }

        itemService.unlinkSource(item, source)

        render([action: DELETE_ACTION] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }
}
