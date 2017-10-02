package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BasePersistent
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.validation.Validateable
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException
/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String REFERER = 'referer'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String ITEM_IMAGE_EDIT_FAILED_MESSAGE = 'item.image.edit.failed.message'
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
            editItemImage: 'POST',
            editItemPage: 'GET',
            addItemImages: 'POST',
            deleteItemImage: 'DELETE',
    ]

    ItemService itemService
    UserService userService
    ImageService imageService
    SourceService sourceService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService

    def stash(String id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(ItemGroupCommand command) {
        Closure itemGroupClosure = {
            Map properties = command.properties + [user: userService.currentUser]
            properties as ItemGroup
        }

        processRequest(command, itemGroupClosure) { object ->
            itemService.createGroup(object as ItemGroup)
        }
    }

    def group(String id) {
        if (id) {
            ItemGroup itemGroup = itemService.getGroup id

            if (itemGroup) {
                User user = itemGroup.user

                [
                        user: user,
                        itemGroup: itemGroup,
                        userProfile: user.userProfile,
                        editAllowed: user.id == userService.currentUserId,
                ]
            }
        }
    }

    def createItem(ItemCommand command) {
        Closure itemClosure = {
            Map properties = command.properties + [itemGroup: itemService.getGroup(params.groupId)]
            properties as Item
        }

        processRequest(command, itemClosure) { object ->
            itemService.updateItem(object as Item, command.imageUploadBeans)
        }
    }

    def addItemImages(ItemImageUploadCommand command) {
        Closure itemClosure = {
            itemService.getItem(params.itemId)
        }

        processRequest(command, itemClosure) { object ->
            itemService.updateItem(object as Item, command.imageUploadBeans)
        }
    }

    def deleteItemImage(String itemId, String imageId) {
        Item item = itemService.getItem itemId
        Image image = imageService.getImage imageId
        itemService.deleteItemImage(item, image)
        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
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
                        sources: sourceService.getSourcesByPeriod(item.period)
                ]
            }
        }
    }

    def deleteItem(String id) {
        Item item = itemService.getItem id

        if (item) {
            itemService.deleteItem item
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'group', id: item.itemGroup.id))
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
        }
    }

    def deleteGroup(String id) {
        ItemGroup itemGroup = itemService.getGroup id

        if (itemGroup) {
            itemService.deleteGroup itemGroup
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'stash'))
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
        }
    }

    def editItemPage(String id) {
        Item item = itemService.getItem id
        ItemGroup itemGroup = item.itemGroup
        User user = itemGroup.user

        if (user.id == userService.currentUserId) {
            [item: item, itemGroup: itemGroup, user: user, userProfile: user.userProfile, editAllowed: Boolean.TRUE]
        } else {
            throw new AccessDeniedException('')
        }
    }

    def editItemImage(ImageUploadBean imageUploadBean) {
        Item item = itemService.getItem params.itemId
        Image image = imageService.getImage params.imageId
        item = itemService.editItemImage(item, image, imageUploadBean)

        if (!item.hasErrors()) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, ITEM_IMAGE_EDIT_FAILED_MESSAGE)
        }
    }

    def editItemField() {
        Item item = itemService.getItem params.objectId

        if (item) {
            String fieldName = params.fieldName
            String fieldValue = params.fieldValue
            item = itemService.editItemField(item, fieldName, fieldValue)

            if (!item.hasErrors()) {
                render([success: Boolean.TRUE] as JSON)
            } else {
                render ajaxResponseService.renderValidationResponse(item)
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE)
        }
    }

    def editItemGroupField() {
        ItemGroup itemGroup = itemService.getGroup params.objectId

        if (itemGroup) {
            String fieldName = params.fieldName
            String fieldValue = params.fieldValue
            itemGroup = itemService.editItemGroupField(itemGroup, fieldName, fieldValue)

            if (!itemGroup.hasErrors()) {
                render([success: Boolean.TRUE] as JSON)
            } else {
                render ajaxResponseService.renderValidationResponse(itemGroup)
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE)
        }
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }

    private processRequest(Validateable command, Closure<? extends BasePersistent> objectClosure, Closure<? extends BasePersistent> action) {
        if (command.validate()) {
            BasePersistent object = objectClosure()

            if (object) {
                if (!action(object).hasErrors()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(object)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
