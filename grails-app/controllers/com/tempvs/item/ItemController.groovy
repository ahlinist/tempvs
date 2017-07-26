package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.web.mapping.LinkGenerator

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    static defaultAction = 'stash'

    ItemService itemService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService
    UserService userService

    def stash(String id) {
        User user = id ? userService.getUser(id) : userService.currentUser

        if (user) {
            [user: user, itemGroups: user.itemGroups, userProfile: user.userProfile, editAllowed: id ? user.id == userService.currentUserId : Boolean.TRUE]
        }
    }

    def createGroup(CreateItemGroupCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                ItemGroup itemGroup = itemService.createGroup(command.properties)

                if (itemGroup.validate()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'group', id: itemGroup.id))
                } else {
                    render ajaxResponseService.renderValidationResponse(itemGroup)
                }
            } else {
                render ajaxResponseService.renderValidationResponse(command)
            }
        }
    }

    def group(String id) {
        if (id) {
            ItemGroup itemGroup = itemService.getGroup(id)

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
        if (params.isAjaxRequest) {
            if (command.validate()) {
                ItemGroup itemGroup = itemService.getGroup request.getHeader('referer').tokenize('/').last()
                Item item = itemService.createItem(command.properties + [itemGroup: itemGroup])

                if (item.validate()) {
                    render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: item.id))
                } else {
                    render ajaxResponseService.renderValidationResponse(item)
                }
            } else {
                render ajaxResponseService.renderValidationResponse(command)
            }
        }
    }

    def show(String id) {
        if (id) {
            Item item = itemService.getItem(id)

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
        if (params.isAjaxRequest) {
            Item item = itemService.getItem id

            if (item) {
                ItemGroup itemGroup = item.itemGroup

                if (itemGroup.user.id == userService.currentUserId) {
                    if (itemService.deleteItem(item)) {
                        return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'group', id: itemGroup.id)))
                    }
                }
            }

            render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
        }
    }

    def deleteGroup(String id) {
        if (params.isAjaxRequest) {
            ItemGroup itemGroup = itemService.getGroup id

            if (itemGroup) {
                User user = itemGroup.user
                if (user.id == userService.currentUserId) {
                    if (itemService.deleteGroup(itemGroup)) {
                        return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'stash', id: user.id)))
                    }
                }
            }

            render ajaxResponseService.renderFormMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
        }
    }

    def editItem(ItemCommand command) {
        if (params.isAjaxRequest) {
            Item item = itemService.getItem request.getHeader('referer').tokenize('/').last()
            if (command.validate()) {
                if (item) {
                    Item updatedItem = itemService.updateItem(item, command.properties)

                    if (updatedItem.validate()) {
                        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(action: 'show', id: updatedItem.id))
                    } else {
                        render ajaxResponseService.renderValidationResponse(updatedItem)
                    }
                }
            } else {
                render ajaxResponseService.renderValidationResponse(command)
            }
        }
    }
}
