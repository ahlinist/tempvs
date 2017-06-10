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
    private static final String ITEM_GROUP = 'itemGroup'
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
            [itemStash: itemStash, userProfile: itemStash?.user?.userProfile]
        } else {
            User user = userService.currentUser
            [itemStash: user.itemStash, userProfile: user.userProfile]
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
                session.setAttribute(ITEM_GROUP, itemGroup)

                [
                        itemGroup: itemGroup,
                        itemStash: itemStash,
                        userProfile: user?.userProfile,
                        ownGroup: user.id == userService.currentUserId,
                ]
            }
        }
    }

    def createItem(CreateItemCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                User user = userService.currentUser
                String name = command.name
                String description = command.description
                MultipartFile multipartItemImage = command.itemImage
                MultipartFile multipartSourceImage = command.sourceImage
                ItemGroup itemGroup = session.getAttribute(ITEM_GROUP) as ItemGroup
                Map metaData = [userId: user.id, properties: [itemGroupId: itemGroup.id]]
                Image itemImage = createImage(multipartItemImage, ITEM_IMAGE_COLLECTION, metaData)
                Image sourceImage = createImage(multipartSourceImage, SOURCE_IMAGE_COLLECTION, metaData)
                Item item = itemService.createItem(name, description, itemImage, sourceImage, itemGroup)

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
                        ownItem: user.id == userService.currentUserId,
                ]
            }
        }
    }

    def deleteItem(String id) {
        if (params.isAjaxRequest) {
            Item item = itemService.getItem id

            if (item) {
                ItemGroup itemGroup = item.itemGroup

                if (itemGroup.itemStash.user == userService.currentUser) {
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

                if (itemStash.user == userService.currentUser) {
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

    private Image createImage(MultipartFile file, String collection, Map metaData) {
        Image result

        if (!file?.empty) {
            InputStream inputStream = file.inputStream

            try {
                result = imageService.createImage(inputStream, collection, metaData)
            } finally {
                inputStream?.close()
            }
        }

        result
    }
}
