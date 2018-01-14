package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.user.ClubProfile
import com.tempvs.user.ProfileHolder
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator

/**
 * A controller that handles operations related to {@link com.tempvs.item.Passport}.
 */
@GrailsCompileStatic
class PassportController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
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
    ProfileHolder profileHolder
    PassportService passportService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        redirect controller: 'profile'
    }

    def createPassport(Passport passport) {
        passport.clubProfile = profileHolder.profile as ClubProfile
        passport = passportService.createPassport(passport)

        if (passport.hasErrors()) {
            return ajaxResponseHelper.renderValidationResponse(passport)
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'passport', action: 'show', id: passport.id))
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
                itemMap: composeItemMap(passport),
                availableItems: itemService.getItemsByPeriod(clubProfile.period),
                editAllowed: clubProfile == profileHolder.profile,
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

        if (!passport || !item) {
            return render([action: NO_ACTION] as JSON)
        }

        Item2Passport item2Passport = passportService.addItem(passport, item, quantity)

        if (item2Passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2Passport))
        }

        List<Item> availableItems = itemService.getItemsByPeriod(item.period)
        Map model = [availableItems: availableItems, itemMap: composeItemMap(passport), passport: passport, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/itemSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def removeItem(Long passportId, Long itemId) {
        Passport passport = passportService.getPassport passportId
        Item item = itemService.getItem itemId

        if (!passport || !item) {
            return render([action: NO_ACTION] as JSON)
        }

        passportService.removeItem(passport, item)
        List<Item> availableItems = itemService.getItemsByPeriod(item.period)
        Map model = [availableItems: availableItems, itemMap: composeItemMap(passport), passport: passport, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/itemSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deletePassport(Long id) {
        Passport passport = passportService.getPassport id
        ClubProfile clubProfile = passport.clubProfile
        passportService.deletePassport(passport)
        Map model = [passports: clubProfile.passports, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/passportList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    private Map<Type,List<Item2Passport>> composeItemMap(Passport passport) {
        passportService.getItem2PassportRelations(passport).groupBy {it.item.type}
    }
}
