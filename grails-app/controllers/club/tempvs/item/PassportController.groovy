package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.communication.CommentService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.user.Profile
import club.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.validation.ValidationException
import grails.web.mapping.LinkGenerator

/**
 * A controller that handles operations related to {@link Passport}.
 */
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
            addImages: 'POST',
            deleteComment: 'DELETE',
            deleteImage: 'DELETE',
            editQuantity: 'POST',
    ]

    UserService userService
    ItemService itemService
    ImageService imageService
    CommentService commentService
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

        Profile profile = userService.currentProfile
        Passport persistentPassport
        List<Image> images = imageService.uploadImages(imageUploadBeans, PASSPORT_COLLECTION)

        try {
            persistentPassport = passportService.createPassport(passport, profile, images)

            if (persistentPassport.hasErrors()) {
                throw new ValidationException("Passport has errors", persistentPassport.errors)
            }
        } catch (ValidationException e) {
            imageService.deleteImages(images)
            return render(ajaxResponseHelper.renderValidationResponse(e.errors))
        } catch (Exception e) {
            imageService.deleteImages(images)
        }

        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'passport', action: 'show', id: passport.id))
    }

    def show(Long id) {
        Passport passport = passportService.getPassport id

        if (!passport) {
            return [id: id, notFoundMessage: NO_SUCH_PASSPORT]
        }

        Profile profile = passport.profile

        [
                passport: passport,
                images: passport.images,
                profile: profile,
                itemMap: composeItemMap(passport),
                availableItems: itemService.getItemsByPeriod(profile.period),
                editAllowed: profile == userService.currentProfile,
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
        Profile profile = passport.profile
        passportService.deletePassport(passport, profile)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'show', id: profile.id))
    }

    def addComment(Long objectId, String text) {
        Passport passport = passportService.getPassport objectId

        if (!passport || !text) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = userService.currentProfile
        Comment comment = commentService.createComment(text, profile)
        passport = passportService.addComment(passport, comment)

        if (comment.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(comment))
        }

        Map model = [
                object: passport,
                objectId: objectId,
                controllerName: 'passport',
                editAllowed: passport.profile == profile,
        ]

        String template = groovyPageRenderer.render(template: '/communication/templates/comments', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteComment(Long objectId, Long commentId) {
        Passport passport = passportService.getPassport objectId
        Comment comment = commentService.loadComment commentId

        if (! passport) {
            return render([action: NO_ACTION] as JSON)
        }

        passport = passportService.deleteComment(passport, comment)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [
                object: passport,
                objectId: objectId,
                controllerName: 'passport',
                editAllowed: passport.profile == userService.currentProfile,
        ]

        String template = groovyPageRenderer.render(template: '/communication/templates/comments', model: model)
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

    def addImages(ImageUploadCommand command) {
        List<ImageUploadBean> imageUploadBeans = command.imageUploadBeans

        if (!imageUploadBeans || imageUploadBeans.every { it.image.empty }) {
            return render([action: NO_ACTION] as JSON)
        }

        if (!imageUploadBeans.every { it.validate() }) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBeans.find { it.hasErrors() }))
        }

        Passport passport = passportService.getPassport(params.objectId as Long)
        List<Image> images = imageService.uploadImages(imageUploadBeans, PASSPORT_COLLECTION)

        if (!passport || !images) {
            return render([action: NO_ACTION] as JSON)
        }

        images.each { Image image -> passport.addToImages(image) }
        passport = passportService.savePassport(passport)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [
                images: passport.images,
                objectId: params.objectId,
                controllerName: 'passport',
                addingAllowed: Boolean.TRUE,
                deletingAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteImage(Long objectId, Long imageId) {
        Passport passport = passportService.getPassport objectId
        Image image = imageService.getImage imageId

        if (!passport) {
            return render([action: NO_ACTION] as JSON)
        }

        passport = passportService.deleteImage(passport, image)

        if (passport.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(passport))
        }

        Map model = [
                images: passport.images,
                objectId: objectId,
                controllerName: 'passport',
                addingAllowed: Boolean.TRUE,
                deletingAllowed: Boolean.TRUE,
        ]

        String template = groovyPageRenderer.render(template: '/image/templates/modalCarousel', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    private Map<ItemType,List<Item2Passport>> composeItemMap(Passport passport) {
        passportService.getItem2PassportRelations(passport).groupBy {it.item.itemType}
    }
}
