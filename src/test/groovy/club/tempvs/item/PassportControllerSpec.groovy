package club.tempvs.item

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Comment
import club.tempvs.communication.CommentService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageTagLib
import club.tempvs.image.ImageUploadBean
import club.tempvs.image.ImageUploadCommand
import club.tempvs.periodization.Period
import club.tempvs.user.ClubProfile
import club.tempvs.user.ProfileService
import club.tempvs.user.User
import club.tempvs.user.UserInfoHelper
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

class PassportControllerSpec extends Specification implements ControllerUnitTest<PassportController> {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final Long LONG_THREE = 3L
    private static final String TEXT = 'text'
    private static final String NAME = 'name'
    private static final String GET_METHOD = 'GET'
    private static final String POST_METHOD = 'POST'
    private static final String PROFILE_URL = '/profile'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String SUCCESS_ACTION = 'success'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String PASSPORT_COLLECTION = 'passport'

    def json = Mock JSON
    def user = Mock User
    def item = Mock Item
    def image = Mock Image
    def comment = Mock Comment
    def type = GroovyMock Type
    def passport = Mock Passport
    def period = GroovyMock Period
    def clubProfile = Mock ClubProfile
    def item2Passport = Mock Item2Passport
    def imageUploadBean = Mock ImageUploadBean
    def imageUploadCommand = Mock ImageUploadCommand
    def multipartFile = Mock GrailsMockMultipartFile

    def itemService = Mock ItemService
    def imageTagLib = Mock ImageTagLib
    def imageService = Mock ImageService
    def userInfoHelper = Mock UserInfoHelper
    def commentService = Mock CommentService
    def profileService = Mock ProfileService
    def passportService = Mock PassportService
    def groovyPageRenderer = Mock PageRenderer
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.itemService = itemService
        controller.imageTagLib = imageTagLib
        controller.imageService = imageService
        controller.profileService = profileService
        controller.userInfoHelper = userInfoHelper
        controller.commentService = commentService
        controller.passportService = passportService
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        0 * _

        response.redirectedUrl == PROFILE_URL
    }

    void "Test createPassport()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createPassport(passport, imageUploadCommand)

        then:
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> clubProfile
        1 * clubProfile.asType(ClubProfile) >> clubProfile
        1 * imageService.uploadImages([imageUploadBean], PASSPORT_COLLECTION) >> [image]
        1 * passportService.createPassport(passport, clubProfile, [image]) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        1 * passport.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect(_) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test show()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.show(LONG_ONE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passport.clubProfile >> clubProfile
        1 * clubProfile.period >> period
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * itemService.getItemsByPeriod(period) >> [item, item]
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> clubProfile
        1 * passport.images >> [image]
        0 * _

        and:
        result == [
                images: [image],
                clubProfile: clubProfile,
                passport: passport,
                itemMap: [(type): [item2Passport]],
                availableItems: [item, item],
                editAllowed: Boolean.TRUE,
        ]
    }

    void "Test editPassportField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editPassportField(LONG_ONE, NAME, FIELD_VALUE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passportService.editPassportField(passport, NAME, FIELD_VALUE) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test addItem()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.addItem(LONG_ONE, LONG_TWO, LONG_THREE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * itemService.getItem(LONG_TWO) >> item
        1 * passportService.addItem(passport, item, LONG_THREE) >> item2Passport
        1 * item2Passport.hasErrors() >> Boolean.FALSE
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * item.period >> period
        1 * itemService.getItemsByPeriod(period) >> [item]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test removeItem()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.removeItem(LONG_ONE, LONG_TWO)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * itemService.getItem(LONG_TWO) >> item
        1 * passportService.removeItem(passport, item)
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * item.period >> period
        1 * itemService.getItemsByPeriod(period) >> [item]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deletePassport()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deletePassport(LONG_ONE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passport.clubProfile >> clubProfile
        1 * passportService.deletePassport(passport)
        1 * clubProfile.passports >> [passport]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test addComment()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.addComment(LONG_ONE, TEXT)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> clubProfile
        1 * commentService.createComment(TEXT, clubProfile) >> comment
        1 * passportService.addComment(passport, comment) >> passport
        1 * comment.hasErrors() >> Boolean.FALSE
        1 * passport.clubProfile >> clubProfile
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteComment()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteComment(LONG_ONE, LONG_TWO)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * commentService.getComment(LONG_TWO) >> comment
        1 * passportService.deleteComment(passport, comment) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> clubProfile
        1 * passport.clubProfile >> clubProfile
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test editQuantity()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editQuantity(LONG_ONE, LONG_TWO)

        then:
        1 * passportService.getItem2Passport(LONG_ONE) >> item2Passport
        1 * passportService.editQuantity(item2Passport, LONG_TWO) >> item2Passport
        1 * item2Passport.hasErrors() >> Boolean.FALSE
        1 * item2Passport.passport >> passport
        2 * item2Passport.item >> item
        1 * item.period >> period
        1 * item.type >> type
        1 * itemService.getItemsByPeriod(period) >> [item]
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test addImages()"() {
        given:
        params.objectId = LONG_ONE
        request.method = POST_METHOD

        when:
        controller.addImages(imageUploadCommand)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * imageUploadBean.image >> multipartFile
        1 * multipartFile.empty >> Boolean.FALSE
        1 * imageUploadCommand.imageUploadBeans >> [imageUploadBean]
        1 * imageService.uploadImages([imageUploadBean], PASSPORT_COLLECTION) >> [image]
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passport.addToImages(image)
        1 * passportService.savePassport(passport) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        1 * passport.images >> [image]
        1 * imageTagLib.modalCarousel(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deleteImage()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deleteImage(LONG_ONE, LONG_TWO)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * imageService.getImage(LONG_TWO) >> image
        1 * passportService.deleteImage(passport, image) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        1 * passport.images >> [image]
        1 * imageTagLib.modalCarousel(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }
}
