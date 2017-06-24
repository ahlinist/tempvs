package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.User
import com.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import org.springframework.web.multipart.MultipartFile

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
@GrailsCompileStatic
class ItemController {

    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'
    private static final String DELETE_ITEM_FAILED_MESSAGE = 'item.delete.failed.message'
    private static final String DELETE_GROUP_FAILED_MESSAGE = 'item.group.delete.failed.message'

    static defaultAction = 'stash'

    ItemService itemService
    ImageService imageService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService
    UserService userService

    def stash(String id) {
        if (id) {
            ItemStash itemStash = itemService.getStash(id)

            if (itemStash) {
                User user = itemStash.user
                [itemStash: itemStash, userProfile: user.userProfile, editAllowed: user.id == userService.currentUserId]
            }
        } else {
            User user = userService.currentUser

            if (user) {
                [itemStash: user.itemStash, userProfile: user.userProfile, editAllowed: user.id == userService.currentUserId]
            } else {
                redirect controller: 'auth'
            }
        }
    }

    def createGroup(CreateItemGroupCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                String name = command.name
                String description = command.description
                ItemGroup itemGroup = itemService.createGroup(name, description)

                if (itemGroup.validate()) {
                    render([redirect: grailsLinkGenerator.link(action: 'group', id: itemGroup.id)] as JSON)
                } else {
                    render ajaxResponseService.composeJsonResponse(itemGroup)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }

    def group(String id) {
        if (id) {
            ItemGroup itemGroup = itemService.getGroup(id)

            if (itemGroup) {
                ItemStash itemStash = itemGroup?.itemStash
                User user = itemStash?.user

                [
                        itemGroup: itemGroup,
                        itemStash: itemStash,
                        userProfile: user?.userProfile,
                        editAllowed: user.id == userService.currentUserId,
                ]
            }
        }
    }

    def createItem(ItemCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                ItemGroup itemGroup = itemService.getGroup request.getHeader('referer').tokenize('/').last()
                Map metaData = [userId: userService.currentUserId, properties: [itemGroupId: itemGroup.id]]

                Map properties = [
                        name: command.name,
                        description: command.description,
                        itemImage: imageService.createImage(command.itemImage, ITEM_IMAGE_COLLECTION, metaData),
                        sourceImage: imageService.createImage(command.sourceImage, SOURCE_IMAGE_COLLECTION, metaData),
                        itemGroup: itemGroup,
                ]

                Item item = itemService.createItem(properties)

                if (item.validate()) {
                    render([redirect: grailsLinkGenerator.link(action: 'show', id: item.id)] as JSON)
                } else {
                    render ajaxResponseService.composeJsonResponse(item)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }

    def show(String id) {
        if (id) {
            Item item = itemService.getItem(id)

            if (item) {
                ItemGroup itemGroup = item?.itemGroup
                ItemStash itemStash = itemGroup?.itemStash
                User user = itemStash?.user

                [
                        item: item,
                        itemGroup: itemGroup,
                        itemStash: itemStash,
                        userProfile: user?.userProfile,
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

                if (itemGroup.itemStash.user.id == userService.currentUserId) {
                    if (itemService.deleteItem(item)) {
                        render([redirect: grailsLinkGenerator.link(action: 'group', id: itemGroup.id)] as JSON)
                    } else {
                        render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
                    }
                } else {
                    render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
                }
            } else {
                render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_ITEM_FAILED_MESSAGE)
            }
        } else {
            redirect action: 'stash'
        }
    }

    def deleteGroup(String id) {
        if (params.isAjaxRequest) {
            ItemGroup itemGroup = itemService.getGroup id

            if (itemGroup) {
                ItemStash itemStash = itemGroup.itemStash

                if (itemStash.user.id == userService.currentUserId) {
                    if (itemService.deleteGroup(itemGroup)) {
                        render([redirect: grailsLinkGenerator.link(action: 'stash', id: itemStash.id)] as JSON)
                    } else {
                        render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
                    }
                } else {
                    render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
                }
            } else {
                render ajaxResponseService.renderMessage(Boolean.FALSE, DELETE_GROUP_FAILED_MESSAGE)
            }
        } else {
            redirect action: 'stash'
        }
    }

    def editItem(ItemCommand command) {
        if (params.isAjaxRequest) {
            Item item = itemService.getItem request.getHeader('referer').tokenize('/').last()
            if (command.validate()) {
                if (item) {
                    Map metaData = [userId: userService.currentUserId, properties: [itemGroupId: item.itemGroup.id]]
                    Map properties = [name: command.name, description: command.description]
                    MultipartFile multipartItemImage = command.itemImage
                    MultipartFile multipartSourceImage = command.sourceImage

                    if (!multipartItemImage.empty) {
                        Image itemImage = imageService.replaceImage(ITEM_IMAGE_COLLECTION, item.itemImageId, multipartItemImage, metaData)
                        properties.itemImageId = itemImage.id
                    }

                    if (!multipartSourceImage.empty) {
                        Image sourceImage = imageService.replaceImage(SOURCE_IMAGE_COLLECTION, item.sourceImageId, multipartSourceImage, metaData)
                        properties.sourceImageId = sourceImage.id
                    }

                    Item updatedItem = itemService.updateItem(item, properties)

                    if (updatedItem.validate()) {
                        render([redirect: grailsLinkGenerator.link(action: 'show', id: updatedItem.id)] as JSON)
                    } else {
                        render ajaxResponseService.composeJsonResponse(itemService.updateItem(item, properties))
                    }
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }
}
