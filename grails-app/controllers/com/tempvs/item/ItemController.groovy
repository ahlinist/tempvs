package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable
import grails.web.mapping.LinkGenerator
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.security.access.AccessDeniedException

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String REFERER = 'referer'
    private static final String ITEM_COLLECTION = 'item'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String ITEM_IMAGE_EDIT_FAILED_MESSAGE = 'item.image.edit.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    static defaultAction = 'stash'

    static allowedMethods = [
            stash: 'GET',
            createGroup: 'POST',
            editGroup: 'POST',
            group: 'GET',
            createItem: 'POST',
            show: 'GET',
            deleteItem: 'DELETE',
            deleteGroup: 'DELETE',
            editItem: 'POST',
            editItemImage: 'POST',
            editItemPage: 'GET',
            addItemImages: 'POST',
            deleteItemImage: 'DELETE',
    ]

    ItemService itemService
    UserService userService
    ImageService imageService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService

    def stash(String id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(ItemGroupCommand command) {
        Closure successAction = { Validateable entity ->
            itemService.saveGroup(entity as ItemGroup)
        }

        processEntity(command, successAction) {
            Map properties = command.properties + [user: userService.currentUser]
            properties as ItemGroup
        }
    }

    def editGroup(ItemGroupCommand command) {
        Closure successAction = { Validateable entity ->
            itemService.saveGroup(entity as ItemGroup)
        }

        processEntity(command, successAction) {
            ItemGroup itemGroup = itemService.getGroup params.groupId

            if (itemGroup) {
                InvokerHelper.setProperties(itemGroup, command.properties)
                itemGroup
            }
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
        Closure successAction = { Validateable entity ->
            List<Image> images = imageService.uploadImages(command.imageUploadBeans, ITEM_COLLECTION)
            itemService.saveItem(entity as Item, images)
        }

        processEntity(command, successAction) {
            Map properties = command.properties + [itemGroup: itemService.getGroup(params.groupId)]
            properties as Item
        }
    }

    def editItem(ItemCommand command) {
        Closure successAction = { Validateable entity ->
            itemService.saveItem(entity as Item)
        }

        processEntity(command, successAction) {
            Item item = itemService.getItem params.itemId

            if (item) {
                InvokerHelper.setProperties(item, command.properties)
                item
            }
        }
    }

    def addItemImages(ItemImageUploadCommand command) {
        Closure successAction = { Validateable entity ->
            List<Image> images = imageService.uploadImages(command.imageUploadBeans, ITEM_COLLECTION)
            itemService.saveItem(entity as Item, images)
        }

        processEntity(command, successAction) {
            itemService.getItem(params.itemId)
        }
    }

    def deleteItemImage(String itemId, String imageId) {
        Item item = itemService.getItem(itemId)
        Image image = imageService.getImage(imageId)
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
                ]
            }
        }
    }

    def deleteItem(String id) {
        Item item = itemService.getItem id

        if (item && itemService.deleteItem(item)) {
            return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'group', id: item.itemGroup.id)))
        }

        render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
    }

    def deleteGroup(String id) {
        ItemGroup itemGroup = itemService.getGroup id

        if (itemGroup && itemService.deleteGroup(itemGroup)) {
            return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'stash')))
        }

        render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
    }

    def editItemPage(String id) {
        Item item = itemService.getItem id
        ItemGroup itemGroup = item.itemGroup
        User user = itemGroup.user

        if (user.id == userService.currentUserId) {
            [item: item, itemGroup: itemGroup, user: user, userProfile: user.userProfile]
        } else {
            throw new AccessDeniedException('')
        }
    }

    def editItemImage(ImageUploadBean imageUploadBean) {
        Item item = itemService.getItem(params.itemId)
        Image image = imageService.getImage(params.imageId)
        imageService.updateImage(imageUploadBean, image.collection, image)

        if (itemService.saveItem(item)) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, ITEM_IMAGE_EDIT_FAILED_MESSAGE)
        }
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }

    private processEntity(Validateable command, Closure successAction, Closure generateEntity) {
        if (command.validate()) {
            Validateable entity = generateEntity() as Validateable

            if (entity) {
                if (entity.validate()) {
                    successAction(entity)
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(entity)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
