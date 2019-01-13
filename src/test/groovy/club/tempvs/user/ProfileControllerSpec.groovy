package club.tempvs.user

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Following
import club.tempvs.communication.FollowingService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.periodization.Period
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.validation.Errors
import spock.lang.Specification

class ProfileControllerSpec extends Specification implements ControllerUnitTest<ProfileController>, DomainUnitTest<Profile> {

    private static final String ONE = '1'
    private static final Integer OFFSET = 0
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String QUERY = 'query'
    private static final String GET_METHOD = 'GET'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String AUTH_URL = '/auth/index'
    private static final String FIELD_NAME = 'fieldName'
    private static final String IDENTIFIER = 'identifier'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String PROFILE_URL = '/profile/index'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def errors = Mock Errors
    def following = Mock Following
    def period = GroovyMock Period
    def profile = Mock Profile
    def imageUploadBean = Mock ImageUploadBean
    def emailVerification = Mock EmailVerification

    def userService = Mock UserService
    def imageService = Mock ImageService
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.verifyService = verifyService
        controller.profileService = profileService
        controller.followingService = followingService
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * userService.currentUser >> user
        1 * user.userProfile >> profile
        1 * user.currentProfile >> profile
        1 * profile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/show/${IDENTIFIER}"
    }

    void "Test search()"() {
        given:
        params.query = QUERY
        params.offset = OFFSET
        request.method = GET_METHOD
        String name = 'name'

        when:
        controller.search()

        then:
        1 * userService.currentProfile >> profile
        1 * profileService.searchProfiles(profile, QUERY, OFFSET) >> [profile]
        1 * profile.id >> LONG_ID
        1 * profile.toString() >> name
        0 * _

        and:
        response.json.size() == 1
        response.json[0].id == LONG_ID
        response.json[0].name == name
    }

    void "Test show()"() {
        given:
        request.method = GET_METHOD

        when:
        params.id = ONE
        def result = controller.show()

        then:
        1 * userService.currentProfile >> profile
        1 * profileService.getProfile(ONE) >> profile
        1 * profile.user >> user
        1 * profile.isOfUserType() >> true
        1 * user.clubProfiles >> [profile]
        2 * profile.active >> true
        1 * profile.identifier >> IDENTIFIER
        1 * followingService.mayBeFollowed(profile, profile) >> true
        1 * followingService.getFollowing(profile, profile) >> following
        1 * following.asType(Boolean) >> true
        0 * _

        and:
        result == [
                profile: profile,
                user: user,
                id: IDENTIFIER,
                editAllowed: true,
                mayBeFollowed: true,
                isFollowed: true,
                activeProfiles: [profile],
                inactiveProfiles: [],
                periods: Period.values(),
                currentProfile: profile,
        ]
    }

    void "Test show() with no id and being not logged in"() {
        given:
        request.method = GET_METHOD

        when:
        controller.show()

        then:
        1 * userService.currentProfile
        0 * _

        and:
        response.redirectedUrl == AUTH_URL
    }

    void "Test show() with no id being logged in"() {
        given:
        request.method = GET_METHOD

        when: 'No id given'
        controller.show()

        then:
        1 * userService.currentProfile >> profile
        1 * profile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/show/${IDENTIFIER}"
    }

    void "Test show() for non-existent profile"() {
        given:
        request.method = GET_METHOD

        when:
        params.id = ONE
        def result = controller.show()

        then:
        1 * userService.currentProfile >> profile
        1 * profileService.getProfile(ONE) >> null
        0 * _

        and:
        result == [id: ONE, notFoundMessage: NO_SUCH_PROFILE]
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.profiles >> [profile]
        1 * profile.type >> ProfileType.USER
        1 * profile.active >> true
        1 * profileService.setCurrentProfile(user, profile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test switchProfile() being logged in with id"() {
        given:
        params.id = LONG_ID

        when:
        controller.switchProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.profiles >> [profile]
        1 * profile.id >> LONG_ID
        1 * profile.active >> true
        1 * profileService.setCurrentProfile(user, profile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test createProfile() against invalid profile"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createProfile(profile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> true
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * userService.currentUser >> user
        1 * profile.profileEmail >> EMAIL
        1 * profile.setProfileEmail(null)
        1 * profileService.createProfile(user, profile, image) >> profile
        1 * profile.hasErrors() >> true
        1 * profile.errors >> errors
        1 * errors.allErrors >> []
        1 * imageService.deleteImage(image)
        1 * ajaxResponseHelper.renderValidationResponse(errors) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createProfile()"() {
        given:
        request.method = POST_METHOD

        when: 'Command valid. Profile created.'
        controller.createProfile(profile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> true
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * userService.currentUser >> user
        1 * profile.profileEmail >> EMAIL
        1 * profile.setProfileEmail(null)
        1 * profileService.createProfile(user, profile, image) >> profile
        1 * profile.hasErrors() >> false
        1 * profile.id >> LONG_ID
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * verifyService.sendEmailVerification(emailVerification)
        1 * profileService.setCurrentProfile(user, profile)
        1 * profile.id >> LONG_ID
        1 * ajaxResponseHelper.renderRedirect("/profile/show/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editProfileField()"() {
        given:
        params.fieldName = FIELD_NAME
        params.fieldValue = FIELD_VALUE
        request.method = POST_METHOD

        when:
        controller.editProfileField()

        then:
        1 * userService.currentProfile >> profile
        1 * profileService.editProfileField(profile, FIELD_NAME, FIELD_VALUE) >> profile
        1 * profile.hasErrors() >> false
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test editProfileEmail()"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        controller.editProfileEmail()

        then:
        1 * userService.currentProfile >> profile
        1 * profile.id >> LONG_ID
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> false
        1 * verifyService.sendEmailVerification(emailVerification) >> true
        1 * ajaxResponseHelper.renderFormMessage(true, 'profileEmail.verification.sent.message') >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deactivateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.deactivateProfile()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * userService.currentProfile >> profile
        1 * userService.currentUser >> user
        1 * profileService.setCurrentProfile(user, null)
        1 * profileService.deactivateProfile(profile) >> profile
        1 * profile.hasErrors() >> false
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test activateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.activateProfile()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * profile.active >> false
        1 * profileService.activateProfile(profile) >> profile
        1 * profile.hasErrors() >> false
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteAvatar()"() {
        given:
        params.profileId = ONE
        request.method = DELETE_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.deleteAvatar()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * profileService.deleteAvatar(profile)
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test uploadAvatar()"() {
        given:
        params.profileId = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.uploadAvatar(imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> true
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * userService.currentProfile >> profile
        1 * profileService.uploadAvatar(profile, image) >> profile
        1 * profile.hasErrors() >> false
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }

    void "Test getProfileDropdown()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.getProfileDropdown()

        then:
        1 * userService.currentUser >> user
        1 * profileService.getProfileDropdown(user)
        0 * _
    }
}
