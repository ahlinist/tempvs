package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
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

    private static final String ITEM_COLLECTION = 'item'
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
    AjaxResponseService ajaxResponseService

    def stash(String id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(ItemGroupCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseService.renderValidationResponse(command))
        }

        ItemGroup itemGroup = itemService.createGroup(command.properties as ItemGroup)

        if (itemGroup.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(itemGroup))
        }

        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'item', action: 'group', id: itemGroup.id))
    }

    def group(String id) {
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
            return render(ajaxResponseService.renderValidationResponse(command))
        }

        List<Image> images = imageService.uploadImages(command.imageUploadBeans, ITEM_COLLECTION)
        Item item = itemService.updateItem(command.properties as Item, images)

        if (item.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(item))
        }

        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'item', action: 'show', id: item.id))
    }

    def deleteImage() {
        Item item = itemService.getItem params.itemId
        Image image = imageService.getImage params.imageId

        if (item && image) {
            Item persistedItem = itemService.deleteImage(item, image)

            if (!persistedItem.hasErrors()) {
                render([delete: Boolean.TRUE, selector: params.selector] as JSON)
            } else {
                render ajaxResponseService.renderValidationResponse(persistedItem)
            }
        } else {
            render([success: Boolean.FALSE] as JSON)
        }
    }

    def show(String id) {
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
                        sources: item.sources.sort {it.id},
                        availableSources: sourceService.getSourcesByPeriod(item.period),
                ]
            }
        }
    }

    def deleteItem(String id) {
        Item item = itemService.getItem id

        if (!item) {
            return render(ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE))
        }

        itemService.deleteItem item
        render([delete: Boolean.TRUE, selector: params.selector] as JSON)
    }

    def deleteGroup(String id) {
        ItemGroup itemGroup = itemService.getGroup id

        if (!itemGroup) {
            return render(ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE))
        }

        itemService.deleteGroup itemGroup
        render([delete: Boolean.TRUE, selector: params.selector] as JSON)
    }

    def addImage(ImageUploadBean imageUploadBean) {
        Item item = itemService.getItem params.itemId
        Image image = imageService.uploadImage(imageUploadBean, ITEM_COLLECTION)

        if (item && image) {
            item = itemService.updateItem(item, [image])

            if (!item.hasErrors()) {
                Map model = [image: image, itemId: params.itemId]
                String template = groovyPageRenderer.render(template: '/item/templates/addImageForm', model: model)
                render([append: Boolean.TRUE, template: template, selector: params.selector] as JSON)
            } else {
                render ajaxResponseService.renderValidationResponse(item)
            }

        } else {
            render([success: Boolean.FALSE] as JSON)
        }
    }

    def editItemField() {
        Item item = itemService.getItem params.objectId

        if (!item) {
            return render(ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        String fieldName = params.fieldName
        String fieldValue = params.fieldValue
        item = itemService.editItemField(item, fieldName, fieldValue)

        if (item.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(item))
        }

        render([success: Boolean.TRUE] as JSON)
    }

    def editItemGroupField() {
        ItemGroup itemGroup = itemService.getGroup params.objectId

        if (!itemGroup) {
            return render(ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        String fieldName = params.fieldName
        String fieldValue = params.fieldValue
        itemGroup = itemService.editItemGroupField(itemGroup, fieldName, fieldValue)

        if (itemGroup.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(itemGroup))
        }

        render([success: Boolean.TRUE] as JSON)
    }

    def linkSource() {
        Item item = itemService.getItem params.itemId
        Source source = sourceService.getSource params.sourceId

        if (!item || !source || (item.sources.contains(source))) {
            return render([success: Boolean.FALSE] as JSON)
        }

        item = itemService.linkSource(item, source)

        if (item.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(item))
        }

        Map model = [editAllowed: Boolean.TRUE, source: source, itemId: params.itemId]
        String template = groovyPageRenderer.render(template: '/item/templates/linkedSource', model: model)
        render([append: Boolean.TRUE, template: template, selector: params.selector] as JSON)
    }

    def unlinkSource() {
        Item item = itemService.getItem params.itemId
        Source source = sourceService.getSource params.sourceId

        if (!item || !source) {
            return render([success: Boolean.FALSE] as JSON)
        }

        item = itemService.unlinkSource(item, source)

        if (item.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(item))
        }

        render([delete: Boolean.TRUE, selector: params.selector] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }
}
