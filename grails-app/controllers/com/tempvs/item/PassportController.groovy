package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.user.ClubProfile
import com.tempvs.user.ProfileHolder
import com.tempvs.user.UserService
import grails.converters.JSON
import grails.gsp.PageRenderer

/**
 * A controller that handles operations related to {@link com.tempvs.item.Passport}.
 */
class PassportController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String NO_SUCH_PASSPORT = 'passport.noSuchPassport.message'
    private static final String OPERATION_FAILED_MESSAGE = 'operation.failed.message'

    static allowedMethods = [
            createPassport: 'POST',
            show: 'GET',
            editPassportField: 'POST',
            addItem: 'POST',
            removeItem: 'DELETE',
            deletePassport: 'DELETE',
    ]

    ItemService itemService
    UserService userService
    ProfileHolder profileHolder
    PassportService passportService
    PageRenderer groovyPageRenderer
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        redirect controller: 'profile'
    }

    def createPassport(Passport passport) {
        passport.clubProfile = profileHolder.profile
        passport = passportService.createPassport(passport)

        if (passport.hasErrors()) {
            return ajaxResponseHelper.renderValidationResponse(passport)
        }

        render ajaxResponseHelper.renderRedirect(g.createLink(controller: 'passport', action: 'show', id: passport.id))
    }

    def show(String id) {
        Passport passport = passportService.getPassport id

        if (!passport) {
            return [id: id, notFoundMessage: NO_SUCH_PASSPORT]
        }

        ClubProfile clubProfile = passport.clubProfile

        [
                clubProfile: clubProfile,
                passport: passport,
                itemMap: prepareItemMap(passport),
                availableItems: itemService.getItemsByPeriod(clubProfile.period),
                editAllowed: clubProfile.user == userService.currentUser,
        ]
    }

    def editPassportField(Long objectId, String fieldName, String fieldValue) {
        Passport passport = passportService.getPassport objectId

        if (!passport) {
            return render(ajaxResponseHelper.renderFormMessage(Boolean.FALSE, OPERATION_FAILED_MESSAGE))
        }

        passport = passportService.editPassportField(passport, fieldName, fieldValue)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def addItem(Long passportId, Long itemId, Long quantity) {
        Passport passport = passportService.getPassport passportId
        Item item = itemService.getItem itemId

        if (!passport || !item || !quantity) {
            return render([action: NO_ACTION] as JSON)
        }

        Item2Passport item2Passport = passportService.addItem(passport, item, quantity)

        if (item2Passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2Passport))
        }

        Map model = [itemMap: prepareItemMap(passport), editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/itemButton', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def removeItem(Long passportId, Long itemId) {
        Passport passport = passportService.getPassport passportId
        Item item = itemService.getItem itemId

        if (!passport || !item) {
            return render([action: NO_ACTION] as JSON)
        }

        passportService.removeItem(passport, item)

        Map model = [itemMap: prepareItemMap(passport), editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/itemButton', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deletePassport(Long id) {
        Passport passport = passportService.getPassport id
        passportService.deletePassport(passport)
        render([action: DELETE_ACTION] as JSON)
    }

    private Map<Type, Item2Passport> prepareItemMap(Passport passport) {
        List<Item2Passport> items2Passport = itemService.getItemsByPassport(passport)

        items2Passport.groupBy { item2Passport ->
            item2Passport.item.type
        }
    }
}
