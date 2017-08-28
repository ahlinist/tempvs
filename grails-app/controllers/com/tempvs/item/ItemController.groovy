package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
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
    private static final String NO_ITEM_FOUND = 'item.notFound.message'
    private static final String NO_GROUP_FOUND = 'item.group.notFound.message'
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
        processItemGroup(command) {
            Map properties = command.properties + [user: userService.currentUser]
            properties as ItemGroup
        }
    }

    def editGroup(ItemGroupCommand command) {
        processItemGroup(command) {
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
        processItem(command) {
            Map properties = command.properties + [itemGroup: itemService.getGroup(params.groupId)]
            properties as Item
        }
    }

    def editItem(ItemCommand command) {
        processItem(command) {
            Item item = itemService.getItem params.itemId

            if (item) {
                InvokerHelper.setProperties(item, command.properties)
                item
            }
        }
    }

    def addItemImages(ItemImageUploadCommand command) {
        processItem(command) {
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

    private processItem(BaseItemCommand command, Closure generateItem) {
        if (command.validate()) {
            Item item = generateItem() as Item

            if (item) {
                if (item.validate()) {
                    List<Image> images = imageService.uploadImages(command.imageUploadBeans, ITEM_COLLECTION)
                    itemService.saveItem(item, images)
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(item)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_ITEM_FOUND)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    private processItemGroup(ItemGroupCommand command, Closure generateGroup) {
        if (command.validate()) {
            ItemGroup itemGroup = generateGroup() as ItemGroup

            if (itemGroup) {
                if (itemGroup.validate()) {
                    itemService.saveGroup(itemGroup)
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(itemGroup)
                }
            } else {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_GROUP_FOUND)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }
}
