package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String REFERER = 'referer'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'
    private static final String EDIT_GROUP_FAILED_MESSAGE = 'item.group.edit.failed.message'

    static defaultAction = 'stash'

    static allowedMethods = [stash: 'GET', createGroup: 'POST', editGroup: 'POST', group: 'GET', createItem: 'POST', show: 'GET', deleteItem: 'DELETE', deleteGroup: 'DELETE', editItem: 'POST']

    ItemService itemService
    UserService userService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService

    def stash(String id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(ItemGroupCommand command) {
        if (command.validate()) {
            ItemGroup itemGroup = itemService.createGroup(command.properties as ItemGroup)

            if (itemGroup.validate()) {
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'group', id: itemGroup.id))
            } else {
                render ajaxResponseService.renderValidationResponse(itemGroup)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def editGroup(ItemGroupCommand command) {
        if (command.validate()) {
            ItemGroup itemGroup = itemService.getGroup params.groupId

            if (itemGroup) {
                ItemGroup editedGroup = itemService.editGroup(itemGroup, command.properties)

                if (editedGroup.validate()) {
                    return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER))))
                }
            }

            render ajaxResponseService.renderFormMessage(Boolean.FALSE, EDIT_GROUP_FAILED_MESSAGE)
        } else {
            render ajaxResponseService.renderValidationResponse(command)
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
        if (command.validate()) {
            ItemGroup itemGroup = itemService.getGroup params.groupId
            Map properties = command.properties + [itemGroup: itemGroup]
            Item item = itemService.createItem(properties as Item, properties)

            if (item.validate()) {
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: item.id))
            } else {
                render ajaxResponseService.renderValidationResponse(item)
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
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

    def editItem(ItemCommand command) {
        Item item = itemService.getItem params.itemId

        if (command.validate()) {
            if (item) {
                Item editedItem = itemService.editItem(item, command.properties)

                if (editedItem.validate()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(uri: request.getHeader(REFERER)))
                } else {
                    render ajaxResponseService.renderValidationResponse(editedItem)
                }
            }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
