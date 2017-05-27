package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BaseObject
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.user.User
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.web.multipart.MultipartFile

/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
class ItemController {

    private static final String ITEM_IMAGE_COLLECTION = 'item'
    private static final String SOURCE_IMAGE_COLLECTION = 'source'

    static defaultAction = 'stash'

    ItemService itemService
    ImageService imageService
    AjaxResponseService ajaxResponseService
    SpringSecurityService springSecurityService

    def stash(String id) {
        if (id) {
            [itemStash: itemService.getStash(id)]
        } else {
            [itemStash: springSecurityService.currentUser.itemStash]
        }
    }

    def createGroup(CreateItemGroupCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                String name = command.name
                String description = command.description
                ItemGroup itemGroup = itemService.createGroup(name, description)

                if (itemGroup.validate()) {
                    render([redirect: g.createLink(action: 'group', id: itemGroup.id)] as JSON)
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
            session.itemGroup = itemGroup
            [itemGroup: itemGroup]
        }
    }

    def createItem(CreateItemCommand command) {
        if (params.isAjaxRequest) {
            if (command.validate()) {
                User user = springSecurityService.currentUser
                String name = command.name
                String description = command.description
                MultipartFile multipartItemImage = command.itemImage
                MultipartFile multipartSourceImage = command.sourceImage
                ItemGroup itemGroup = session.itemGroup
                Map metaData = [userId: user.id, properties: [itemGroupId: itemGroup.id]]
                Image itemImage = createImage(multipartItemImage, ITEM_IMAGE_COLLECTION, metaData)
                Image sourceImage = createImage(multipartSourceImage, SOURCE_IMAGE_COLLECTION, metaData)
                Item item = itemService.createItem(name, description, itemImage, sourceImage, itemGroup)

                if (item.validate()) {
                    render([redirect: g.createLink(action: 'show', id: item.id)] as JSON)
                } else {
                    render ajaxResponseService.composeJsonResponse(item)
                }
            } else {
                ajaxResponseService.composeJsonResponse(command)
            }
        }
    }

    def show(String id) {
        if (id) {
            [item: itemService.getItem(id)]
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

@GrailsCompileStatic
class CreateItemGroupCommand extends BaseObject {
    String name
    String description

    static constraints = {
        description nullable: true
    }
}

@GrailsCompileStatic
class CreateItemCommand extends BaseObject {
    String name
    String description
    MultipartFile itemImage
    MultipartFile sourceImage

    static constraints = {
        description nullable: true
        itemImage nullable: true
        sourceImage nullable: true
    }
}
