package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BaseObject
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
/**
 * Controller that manages operations with {@link com.tempvs.item.Item}.
 */
class ItemController {

    static defaultAction = 'stash'

    SpringSecurityService springSecurityService
    ItemService itemService
    AjaxResponseService ajaxResponseService

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
            [itemGroup: itemService.getGroup(id)]
        }
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
