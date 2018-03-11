package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.Comment
import com.tempvs.communication.CommentService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.image.ImageUploadCommand
import com.tempvs.user.ClubProfile
import com.tempvs.user.Profile
import com.tempvs.user.ProfileService
import com.tempvs.user.UserInfoHelper
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.annotation.Secured

/**
 * A controller that handles operations related to {@link com.tempvs.item.Passport}.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class PassportController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String PASSPORT_COLLECTION = 'passport'
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
            addComment: 'POST',
            addImage: 'POST',
            deleteComment: 'DELETE',
            deleteImage: 'DELETE',
            editQuantity: 'POST',
    ]

    ItemService itemService
    ImageService imageService
    UserInfoHelper userInfoHelper
    CommentService commentService
    ProfileService profileService
    PassportService passportService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        redirect controller: 'profile'
    }

    def createPassport(Passport passport, ImageUploadCommand command) {
        List<ImageUploadBean> imageUploadBeans = command.imageUploadBeans

        if (imageUploadBeans && !imageUploadBeans.every { it.validate() }) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBeans.find { it.hasErrors() }))
        }

        passport.clubProfile = userInfoHelper.getCurrentProfile(request) as ClubProfile

        if (!passport.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        passport.images = imageService.uploadImages(imageUploadBeans, PASSPORT_COLLECTION)

        passport = passportService.savePassport(passport)

        if (passport.hasErrors()) {
            return ajaxResponseHelper.renderValidationResponse(passport)
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'passport', action: 'show', id: passport.id))
    }

    @Secured('permitAll')
    def show(Long id) {
        Passport passport = passportService.getPassport id

        if (!passport) {
            return [id: id, notFoundMessage: NO_SUCH_PASSPORT]
        }

        ClubProfile clubProfile = passport.clubProfile

        [
                passport: passport,
                images: passport.images,
                clubProfile: clubProfile,
                itemMap: composeItemMap(passport),
                availableItems: itemService.getItemsByPeriod(clubProfile.period),
                editAllowed: clubProfile == userInfoHelper.getCurrentProfile(request),
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

    def addComment(Long passportId, String text) {
        Passport passport = passportService.getPassport passportId

        if (!passport || !text) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)
        Comment comment = commentService.createComment(text, profile)
        passport = passportService.addComment(passport, comment)

        if (comment.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(comment))
        }

        Map model = [passport: passport, currentProfile: profile, editAllowed: passport.clubProfile == profile,]
        String template = groovyPageRenderer.render(template: '/passport/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteComment(Long objectId, Long commentId) {
        Passport passport = passportService.getPassport objectId
        Comment comment = commentService.getComment commentId

        if (! passport || !comment) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)
        passport = passportService.deleteComment(passport, comment)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [
                passport: passport,
                currentProfile: profile,
                editAllowed: passport.clubProfile == profile,
        ]

        String template = groovyPageRenderer.render(template: '/passport/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def editQuantity(Long item2PassportId, Long delta) {
        Item2Passport item2Passport = passportService.getItem2Passport(item2PassportId)
        item2Passport = passportService.editQuantity(item2Passport, delta)

        if (item2Passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(item2Passport))
        }

        Passport passport = item2Passport.passport

        Map model = [
                availableItems: itemService.getItemsByPeriod(item2Passport.item.period),
                itemMap: composeItemMap(passport),
                passport: passport,
                editAllowed: Boolean.TRUE
        ]

        String template = groovyPageRenderer.render(template: '/passport/templates/itemSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def addImage(ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        Passport passport = passportService.getPassport(params.passportId as Long)
        Image image = imageService.uploadImage(imageUploadBean, PASSPORT_COLLECTION)

        if (!passport || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        passport.addToImages(image)
        passport = passportService.savePassport(passport)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [images: passport.images, passportId: params.passportId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/imageSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteImage(Long passportId, Long imageId) {
        Passport passport = passportService.getPassport passportId
        Image image = imageService.getImage imageId

        if (!passport || !image) {
            return render([action: NO_ACTION] as JSON)
        }

        passport = passportService.deleteImage(passport, image)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [images: passport.images, passportId: passportId, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/passport/templates/imageSection', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect(controller: 'auth')
        }
    }

    private Map<Type,List<Item2Passport>> composeItemMap(Passport passport) {
        passportService.getItem2PassportRelations(passport).groupBy {it.item.type}
    }
}
